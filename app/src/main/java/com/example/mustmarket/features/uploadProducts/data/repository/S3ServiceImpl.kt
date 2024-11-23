package com.example.mustmarket.features.uploadProducts.data.repository

import android.content.Context
import android.net.Uri
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.UploadOptions
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.S3Objects.BUCKET_NAME
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.S3Objects.MAX_CONCURRENT_UPLOADS
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.S3Objects.MAX_FILE_SIZE
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.UploadError
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.UploadProgress
import com.example.mustmarket.features.uploadProducts.domain.s3Service.S3Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.withPermit
import java.util.Collections

class S3ServiceImpl @Inject constructor(
    private val context: Context
) : S3Service {
    private val uploadScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val uploadSemaphore = Semaphore(MAX_CONCURRENT_UPLOADS)

    private val s3Client: AmazonS3Client by lazy {
        val credentials = BasicAWSCredentials(
            "",
            ""
        )
        AmazonS3Client(credentials).apply {
            setRegion(Region.getRegion(Regions.DEFAULT_REGION))
        }
    }

    private val transferUtility: TransferUtility by lazy {
        TransferUtility.builder()
            .context(context)
            .s3Client(s3Client)
            .defaultBucket(BUCKET_NAME)
            .build()
    }

    override suspend fun uploadImages(images: List<Uri>): Flow<Resource<List<UploadProgress>>> =
        flow {
            emit(Resource.Loading(true))

            try {
                val progressMap =
                    Collections.synchronizedMap(mutableMapOf<String, UploadProgress>())

                coroutineScope {
                    images.map { uri ->
                        async {
                            uploadSemaphore.withPermit {
                                uploadSingleImage(uri)
                                    .catch { e ->
                                        val error = mapException(e)
                                        val fileName = getFileName(uri)
                                        progressMap[fileName] = UploadProgress(
                                            fileName = fileName,
                                            progress = 0f,
                                            error = error
                                        )
                                    }
                                    .collect { progress ->
                                        progressMap[progress.fileName] = progress
                                        emit(Resource.Success(progressMap.values.toList()))
                                    }
                            }
                        }
                    }.awaitAll()
                }
            } catch (e: Exception) {
                emit(Resource.Error(mapException(e).messages))
            }
        }

    private fun uploadSingleImage(uri: Uri): Flow<UploadProgress> = flow {
        try {
            val fileSize = getFileSize(uri)
            if (fileSize > MAX_FILE_SIZE) {
                throw IOException("File size exceeds maximum limit of $MAX_FILE_SIZE bytes")
            }
            val fileName = getFileName(uri)
            val key = "products/${UUID.randomUUID()}/$fileName"

            val progressChannel = Channel<UploadProgress>()

            val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val observer = transferUtility.upload(
                key,
                tempFile,
                CannedAccessControlList.PublicRead
            )

            observer.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            val url = "https://${BUCKET_NAME}.s3.amazonaws.com/$key"
                            progressChannel.trySend(
                                UploadProgress(
                                    fileName = fileName,
                                    progress = 1f,
                                    url = url,
                                    bytesTransferred = fileSize,
                                    totalBytes = fileSize
                                )
                            )
                            progressChannel.close()
                        }

                        TransferState.FAILED -> {
                            progressChannel.trySend(
                                UploadProgress(
                                    fileName = fileName,
                                    progress = 0f,
                                    error = UploadError.S3Error("Upload Failed")
                                )
                            )
                            progressChannel.close()
                        }

                        else -> {}
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val progress = if (bytesTotal > 0) bytesCurrent.toFloat() / bytesTotal else 0f
                    progressChannel.trySend(
                        UploadProgress(
                            fileName = fileName,
                            progress = progress,
                            bytesTransferred = bytesCurrent,
                            totalBytes = bytesTotal
                        )
                    )
                }

                override fun onError(id: Int, ex: Exception?) {
                    progressChannel.trySend(
                        UploadProgress(
                            fileName = fileName,
                            progress = 0f,
                            error = ex?.let { mapException(it) }
                        )
                    )
                    progressChannel.close()
                }
            })

            for (progress in progressChannel) {
                emit(progress)
            }
        } catch (e: Exception) {
            val error = mapException(e)
            emit(
                UploadProgress(
                    fileName = getFileName(uri),
                    progress = 0f,
                    error = error
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    private fun mapException(e: Throwable): UploadError = when (e) {
        is AmazonS3Exception -> UploadError.S3Error(
            "S3 service error: ${e.errorMessage}"
        )

        is SecurityException -> UploadError.PermissionError(
            "Permission denied: ${e.message ?: "Access denied"}"
        )

        is IOException -> UploadError.StorageError(
            "Storage Error: ${e.message ?: "File access error"} "
        )

        is IllegalArgumentException -> UploadError.InvalidFileError(
            "Invalid file: ${e.message ?: "File validation failed"}"
        )

        else -> UploadError.UnknownError(
            "Unknown error: ${e.message ?: "An unexpected error occurred"}"
        )
    }

    private fun getFileSize(uri: Uri): Long {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex("_size")
            cursor.moveToFirst()
            cursor.getLong(sizeIndex)
        } ?: throw IllegalArgumentException("Unable to determine file size")
    }

    private fun getFileName(uri: Uri): String {
        return context.contentResolver.query(uri, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex("_display_name")
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: uri.lastPathSegment ?: "Unknown_${System.currentTimeMillis()}"
    }


}