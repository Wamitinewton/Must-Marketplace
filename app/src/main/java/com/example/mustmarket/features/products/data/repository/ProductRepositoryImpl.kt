package com.example.mustmarket.features.products.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.mustmarket.core.util.Constants
import com.example.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.di.IODispatcher
import com.example.mustmarket.features.products.data.remote.UploadProductsApi
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.BATCH_SIZE
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.COMPRESSION_QUALITY
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.MAX_FILE_SIZE
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.MAX_IMAGE_DIMENSION
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.RETRY_ATTEMPTS
import com.example.mustmarket.features.products.data.repository.RepositoryConsts.RETRY_DELAY_MS
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: UploadProductsApi,
    @IODispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductRepository {
    override suspend fun uploadListOfImages(images: List<File>): Flow<Resource<UploadImageListOfImageResponse>> =
        flow {
            try {
                emit(Resource.Loading(true))

                val processedImages = images.chunked(BATCH_SIZE).flatMap { batch ->
                    coroutineScope {
                        batch.map { image ->
                            async(dispatcher) {
                                validateAndProcessImage(image)
                            }
                        }.awaitAll()
                    }
                }
                val multipartParts = processedImages.map { file ->
                    file.toMultipartBodyPart("files")
                }
                val response = retryIO(RETRY_ATTEMPTS) {
                    api.uploadListOfImages(multipartParts)
                }
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                } else {
                    emit(Resource.Error(message = response.data.toString()))

                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            } finally {
                emit(Resource.Loading(false))
                cleanUpFiles()
            }
        }.flowOn(dispatcher)

    override suspend fun uploadSingleImages(images: File): Flow<Resource<UploadSingleImageResponse>> =
        flow {
            try {
                val processedImage = validateAndProcessImage(images)
                emit(Resource.Loading(true))
                val response = retryIO(RETRY_ATTEMPTS) {
                    api.uploadSingleImage(processedImage)
                }
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(message = response.data))

                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            } finally {
                emit(Resource.Loading(false))
                cleanUpFiles()
            }
        }.flowOn(dispatcher)

    override suspend fun addProduct(product: UploadProductRequest): Flow<Resource<UploadProductResponse>> =
        flow {
            try {
                emit(Resource.Loading(true))
                val response = retryIO(RETRY_ATTEMPTS) {
                    api.addProducts(product)
                }
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                    emit(Resource.Loading(false))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(message = response.data.toString()))

                }
            } catch (e: Exception) {
                emit(Resource.Loading(false))
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            }
        }.flowOn(dispatcher)

    private suspend fun validateAndProcessImage(file: File): File {
        if (!file.exists()) {
            throw FileNotFoundException("Image file not found: ${file.name}")
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw IOException("File size exceeds maximum allowed size (10MB): ${file.name}")
        }

        return processAndOptimizeImage(file)
    }

    private suspend fun processAndOptimizeImage(file: File): File = withContext(dispatcher) {
        var bitmap: Bitmap? = null
        var scaledBitmap: Bitmap? = null
        var outputFile: File? = null

        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.path, options)

            options.apply {
                inSampleSize = calculateInSampleSize(this)
                inJustDecodeBounds = false
            }

            bitmap = BitmapFactory.decodeFile(file.path, options)
                ?: throw IOException("Failed to decode image: ${file.name}")

            scaledBitmap = scaleBitmap(bitmap)

            outputFile = createTempFile(prefix = "optimized_", suffix = ".jpg")
            FileOutputStream(outputFile).use { out ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, out)
            }

            return@withContext outputFile
        } catch (e: Exception) {
            outputFile?.delete()
            throw e
        } finally {
            if (scaledBitmap != bitmap) {
                scaledBitmap?.recycle()
            }
            bitmap?.recycle()
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (width, height) = options.run { outWidth to outHeight }
        var inSampleSize = 1

        if (height > MAX_IMAGE_DIMENSION || width > MAX_IMAGE_DIMENSION) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfWidth / inSampleSize) > MAX_IMAGE_DIMENSION ||
                (halfHeight / inSampleSize) >= MAX_IMAGE_DIMENSION
            ) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= MAX_IMAGE_DIMENSION && height <= MAX_IMAGE_DIMENSION) {
            return bitmap
        }
        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = MAX_IMAGE_DIMENSION
            newHeight = (newWidth / ratio).toInt()
        } else {
            newHeight = MAX_IMAGE_DIMENSION
            newWidth = (newHeight * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private suspend fun <T> retryIO(
        times: Int,
        initialDelayMillis: Long = RETRY_DELAY_MS,
        block: suspend () -> T
    ): T {
        var currentDelayMillis = initialDelayMillis
        var lastException: Exception? = null

        repeat(times) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e
                if (attempt == times - 1) throw e

                delay(currentDelayMillis)
                currentDelayMillis = (currentDelayMillis * 1.5).toLong()
            }
        }
        throw lastException ?: IllegalStateException("Retry failed")
    }


    private fun cleanUpFiles() {
        try {
            System.getProperty("java.io.tmpdir")?.let {
                File(it).listFiles { file ->
                    file.name.startsWith("optimized_") && file.name.endsWith(".jpg")
                }?.forEach { file ->
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "cleanUpFiles: ${e.message}")
        }
    }

    private fun File.toMultipartBodyPart(name: String): MultipartBody.Part {
        val requestFile = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, this.name, requestFile)
    }

}
