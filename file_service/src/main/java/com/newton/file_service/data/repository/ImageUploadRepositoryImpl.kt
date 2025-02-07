package com.newton.file_service.data.repository

import com.newton.file_service.data.remote.api_service.ImageUploadApi
import com.newton.file_service.data.remote.utils.ValidationException
import com.newton.file_service.domain.model.ImageUploadState
import com.newton.file_service.domain.repository.ImageUploadRepository
import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.toMultiBodyPart
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import timber.log.Timber
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
        private const val MAX_FILE_SIZE = 1000 * 1024 * 1024 // 10MB
        private const val BACKOFF_MULTIPLIER = 1.5
        private val ALLOWED_MIME_TYPES = setOf(
            MimeType.JPEG,
            MimeType.PNG,
            MimeType.WEBP
        )
    }

    private object MimeType {
        const val JPEG = "image/jpeg"
        const val PNG = "image/png"
        const val WEBP = "image/webp"
    }

    override suspend fun uploadSingleImage(
        image: File,
        onProgress: (Int) -> Unit
    ): Flow<ImageUploadState> = flow {
        Timber.d("Starting single image upload: ${image.name}")
        emit(ImageUploadState.Loading)

        validateAndProcessImage(image).fold(
            onSuccess = { processedImage ->
                val progressChannel = Channel<Int>(Channel.CONFLATED)
                try {
                    coroutineScope {
                        launch { collectProgress(progressChannel, onProgress) }

                        val part = createProgressPart(processedImage, "file", progressChannel)
                        val response = try {
                            Timber.i("Attempting to upload image: ${processedImage.name}")
                            retryIO {
                                api.uploadSingleImage(part).also {
                                    Timber.d("Image upload successful: ${processedImage.name}")
                                }
                            }
                        } catch (e: Exception) {
                            Timber.e(e, "Failed to upload single image: ${processedImage.name}")
                            throw e
                        }

                        emit(ImageUploadState.SingleImageSuccess(response.data))
                    }
                } finally {
                    progressChannel.close()
                }
            },
            onFailure = { error ->
                emit(ImageUploadState.Error(error.message ?: "Unknown error"))
                onProgress(-1)
            }
        )
    }.flowOn(dispatcher)

    override suspend fun uploadMultipleImages(
        images: List<File>,
        onProgress: (Int) -> Unit
    ): Flow<ImageUploadState> = flow {
        Timber.d("Starting multiple image upload. Total images: ${images.size}")
        emit(ImageUploadState.Loading)

        val processedImages = images.map { validateAndProcessImage(it) }
        if (processedImages.any { it.isFailure }) {
            val error = processedImages.first { it.isFailure }.exceptionOrNull()
            Timber.e(error, "Image processing failed")
            emit(ImageUploadState.Error(error?.message ?: "Processing failed"))
            onProgress(-1)
            return@flow
        }

        val progressChannel = Channel<Int>(Channel.CONFLATED)
        try {
            coroutineScope {
                val validImages = processedImages.mapNotNull { it.getOrNull() }
                val parts = validImages.map { createProgressPart(it, "files", progressChannel) }
                val totalSize = validImages.sumOf { it.length() }
                Timber.i("Prepared ${validImages.size} images for upload")

                launch { collectProgress(progressChannel, onProgress, totalSize) }

                val response = try {
                    retryIO {
                        api.uploadMultipleImages(parts).also {
                            Timber.d("Multiple images uploaded successfully")
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Failed to upload multiple images")
                    throw e
                }
                emit(ImageUploadState.MultipleImageSuccess(
                    response.data,
                    "Uploaded ${validImages.size} images"
                ))
            }
        } finally {
            progressChannel.close()
        }
    }.flowOn(dispatcher)

    override fun validateImage(file: File): Boolean = runCatching {
        file.length() <= MAX_FILE_SIZE && getMimeType(file) in ALLOWED_MIME_TYPES
    }.getOrDefault(false)

    private fun getMimeType(file: File): String = when (file.extension.lowercase()) {
        "jpg", "jpeg" -> MimeType.JPEG
        "png" -> MimeType.PNG
        "webp" -> MimeType.WEBP
        else -> throw ValidationException("Unsupported file type: ${file.extension}")
    }

    private suspend fun validateAndProcessImage(file: File): Result<File> = runCatching {
        if (!validateImage(file)) {
            throw ValidationException("Invalid image file: ${file.name}")
        }
        fileProcessor.processImage(file).also {
            if (!validateImage(it)) {
                throw ValidationException("Invalid processed image: ${it.name}")
            }
        }
    }

    private fun createProgressPart(
        file: File,
        fieldName: String,
        progressChannel: Channel<Int>
    ): MultipartBody.Part {
        val originalPart = file.toMultiBodyPart(fieldName)
        val contentDisposition = requireNotNull(originalPart.headers?.get("Content-Disposition")) {
            "Missing Content-Disposition header"
        }
        val partName = contentDisposition.substringAfter("name=\"").substringBefore("\"")

        return MultipartBody.Part.createFormData(
            partName,
            file.name,
            ProgressRequestBody(originalPart.body) { progress ->
                progressChannel.trySend(progress)
            }
        )
    }

    private suspend fun collectProgress(
        channel: Channel<Int>,
        onProgress: (Int) -> Unit,
        totalSize: Long? = null
    ) {
        var uploadedBytes: Long
        channel.consumeAsFlow().collect { progress ->
            val finalProgress = if (totalSize != null) {
                uploadedBytes = (totalSize * progress / 100).coerceAtMost(totalSize)
                (uploadedBytes * 100 / totalSize).toInt()
            } else {
                progress
            }
            onProgress(finalProgress)
        }
    }

    private class ProgressRequestBody(
        private val delegate: RequestBody,
        private val onProgress: (Int) -> Unit
    ) : RequestBody() {
        override fun contentType() = delegate.contentType()
        override fun contentLength() = delegate.contentLength()

        override fun writeTo(sink: BufferedSink) {
          try {
              val contentLength = contentLength()
              Timber.d("Starting file upload. Total content length: $contentLength bytes")

              val countingSink = object : ForwardingSink(sink) {
                  private var bytesWritten = 0L

                  override fun write(source: Buffer, byteCount: Long) {
                      super.write(source, byteCount)
                      bytesWritten += byteCount
                      val progress = (bytesWritten * 100 / contentLength)
                          .toInt()
                          .coerceIn(0, 100)
                      onProgress(progress)
                  }
              }

              val bufferedSink = countingSink.buffer()
              delegate.writeTo(bufferedSink)
              bufferedSink.flush()
          } catch (e: Exception){
              throw Exception("Error in progressRequestBody: ${e.message}", e)
          }
        }
    }

    private suspend fun <T> retryIO(
        block: suspend () -> T
    ): T = retry(
        times = MAX_RETRIES,
        initialDelay = INITIAL_BACKOFF,
        factor = BACKOFF_MULTIPLIER,
        block = block
    )

    private suspend fun <T> retry(
        times: Int,
        initialDelay: Long,
        factor: Double = BACKOFF_MULTIPLIER,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                if (e !is IOException) throw e
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong()
            }
        }
        return block() // last attempt
    }
}