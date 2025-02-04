package com.newton.file_service.data.repository

import com.newton.file_service.data.remote.api_service.ImageUploadApi
import com.newton.file_service.data.remote.utils.ImageUploadError
import com.newton.file_service.data.remote.utils.ProcessingException
import com.newton.file_service.data.remote.utils.ValidationException
import com.newton.file_service.domain.model.ImageUploadState
import com.newton.file_service.domain.repository.ImageUploadRepository
import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.toMultiBodyPart
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    private val api: ImageUploadApi,
    private val fileProcessor: FileProcessor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ImageUploadRepository {

    companion object {
        private const val MAX_RETRIES = 3
        private const val INITIAL_BACKOFF = 1000L
        private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB
        private val ALLOWED_MIME_TYPES = setOf("image/jpeg", "image/png", "image/webp")
    }

    override suspend fun uploadSingleImage(
        image: File,
        onProgress: (Int) -> Unit
    ): Flow<ImageUploadState> = callbackFlow {
        send(ImageUploadState.Loading)
        val progressChannel = Channel<Int>(Channel.CONFLATED)

        try {
            if (!validateImage(image)) {
                throw ValidationException("Invalid image file")
            }

            val processedImage = fileProcessor.processImage(image).also {
                if (!validateImage(it)) throw ValidationException("Invalid processed image")
            }

            val part = createProgressPart(processedImage, "file", progressChannel)
            val response = retryIO(MAX_RETRIES) { api.uploadSingleImage(part) }

            launch { collectProgress(this@callbackFlow ,progressChannel, onProgress) }
            send(ImageUploadState.SingleImageSuccess(response.data))
        } catch (e: Exception) {
            sendErrorState(this@callbackFlow ,e, onProgress)
        } finally {
            progressChannel.close()
        }
    }.flowOn(dispatcher)

    override suspend fun uploadMultipleImages(
        images: List<File>,
        onProgress: (Int) -> Unit
    ): Flow<ImageUploadState> = callbackFlow {
        send(ImageUploadState.Loading)
        val progressChannel = Channel<Int>(Channel.CONFLATED)

        try {
            images.forEach { if (!validateImage(it)) throw ValidationException("Invalid file: ${it.name}") }
            val processedImages = images.map { fileProcessor.processImage(it).also { p ->
                if (!validateImage(p)) throw ValidationException("Invalid processed image: ${p.name}")
            }}

            val parts = processedImages.map { createProgressPart(it, "files", progressChannel) }
            launch { collectProgress(this@callbackFlow ,progressChannel, onProgress, processedImages.sumOf { it.length() }) }

            val response = retryIO(MAX_RETRIES) { api.uploadMultipleImages(parts) }
            send(ImageUploadState.MultipleImageSuccess(response.data, "Uploaded ${images.size} images"))
        } catch (e: Exception) {
            sendErrorState(this@callbackFlow ,e, onProgress)
        } finally {
            progressChannel.close()
        }
    }.flowOn(dispatcher)

    override fun validateImage(file: File): Boolean {
        if (file.length() > MAX_FILE_SIZE) return false
        return getMimeType(file) in ALLOWED_MIME_TYPES
    }

    private fun getMimeType(file: File): String? = when (file.extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        else -> null
    }

    private fun createProgressPart(
        file: File,
        fieldName: String,
        progressChannel: Channel<Int>
    ): MultipartBody.Part {
        val originalPart = file.toMultiBodyPart(fieldName)
        val contentDisposition = originalPart.headers!!["Content-Disposition"]!!
        val partName = contentDisposition.split("name=\"")[1].split("\"")[0]

        return MultipartBody.Part.createFormData(
            partName,
            file.name,
            ProgressRequestBody(originalPart.body) { progress ->
                progressChannel.trySend(progress).isSuccess
            }
        )
    }

    private suspend fun collectProgress(
        scope: ProducerScope<ImageUploadState>,
        channel: Channel<Int>,
        onProgress: (Int) -> Unit,
        totalSize: Long? = null
    ) {
        var uploadedBytes = 0L
        for (progress in channel) {
            if (totalSize != null) {
                uploadedBytes = (totalSize * progress / 100).coerceAtMost(totalSize)
                val overallProgress = (uploadedBytes * 100 / totalSize).toInt()
                scope.send(ImageUploadState.Progress(overallProgress))
                onProgress(overallProgress)
            } else {
                scope.send(ImageUploadState.Progress(progress))
                onProgress(progress)
            }
        }
    }

    private suspend fun sendErrorState(
        scope: ProducerScope<ImageUploadState>,
        e: Exception, onProgress: (Int) -> Unit) {
        val error = when (e) {
            is IOException -> ImageUploadError.NetworkError(e.message ?: "Network error")
            is ValidationException -> ImageUploadError.ValidationError(e.message ?: "Validation failed")
            is ProcessingException -> ImageUploadError.FileProcessingError(e.message ?: "Processing failed")
            else -> ImageUploadError.StorageError(e.message ?: "Upload failed")
        }
        scope.send(ImageUploadState.Error(error.toString()))
        onProgress(-1) // Signal error completion
    }

    private inner class ProgressRequestBody(
        private val delegate: RequestBody,
        private val onProgress: (Int) -> Unit
    ) : RequestBody() {
        override fun contentType() = delegate.contentType()
        override fun contentLength() = delegate.contentLength()

        override fun writeTo(sink: BufferedSink) {
            val contentLength = contentLength().takeIf { it > 0 } ?: run {
                delegate.writeTo(sink)
                return
            }

            val countingSink = object : ForwardingSink(sink) {
                private var bytesWritten = 0L

                override fun write(source: Buffer, byteCount: Long) {
                    super.write(source, byteCount)
                    bytesWritten += byteCount
                    val progress = (bytesWritten * 100 / contentLength).toInt().coerceIn(0, 100)
                    onProgress(progress)
                }
            }

            countingSink.buffer().use { delegate.writeTo(it) }
        }
    }

    private suspend fun <T> retryIO(
        times: Int,
        initialDelay: Long = INITIAL_BACKOFF,
        block: suspend () -> T
    ): T {
        var delay = initialDelay
        var lastError: Exception? = null

        repeat(times) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastError = e
                if (e !is IOException || attempt == times - 1) throw e
                kotlinx.coroutines.delay(delay)
                delay = (delay * 1.5).toLong()
            }
        }
        throw lastError ?: IllegalStateException("Retry failed")
    }
}