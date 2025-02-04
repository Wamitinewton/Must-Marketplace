package com.newton.mustmarket.features.merchant.products.data.repository

import com.newton.mustmarket.core.file_config.FileProcessor
import com.newton.mustmarket.core.file_config.toMultiBodyPart
import com.newton.mustmarket.core.util.Constants
import com.newton.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.di.IODispatcher
import com.newton.mustmarket.features.merchant.products.data.remote.UploadProductsApi
import com.newton.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.newton.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadProductResponse
import com.newton.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.newton.mustmarket.features.merchant.products.data.repository.RepositoryConsts.RETRY_ATTEMPTS
import com.newton.mustmarket.features.merchant.products.data.repository.RepositoryConsts.RETRY_DELAY_MS
import com.newton.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import com.newton.mustmarket.features.merchant.products.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: UploadProductsApi,
    @IODispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val fileProcessor: FileProcessor
) : ProductRepository {
    override suspend fun uploadListOfImages(images: List<File>): Flow<Resource<UploadImageListOfImageResponse>> =
        flow {
            try {
                emit(Resource.Loading(true))

                val processedImages = fileProcessor.processImages(images)
                val multipartParts = processedImages.map { it.toMultiBodyPart("files") }
                val response = retryIO(RETRY_ATTEMPTS) {
                    api.uploadListOfImages(multipartParts)
                }
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                    emit(Resource.Loading(false))
                } else {
                    emit(Resource.Error(message = response.message))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            }
        }.flowOn(dispatcher)

    override suspend fun uploadSingleImages(images: File): Flow<Resource<UploadSingleImageResponse>> =
        flow {
            try {
                val processedImage = fileProcessor.processImage(images)
                emit(Resource.Loading(true))
                val response = retryIO(RETRY_ATTEMPTS) {
                    api.uploadSingleImage(processedImage)
                }
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                    emit(Resource.Loading(false))
                } else {
                    emit(Resource.Error(message = response.data))

                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            }
        }.flowOn(dispatcher)

    override suspend fun addProduct(product: UploadProductRequest): Flow<Resource<UploadProductResponse>> =
        flow {
            try {
                emit(Resource.Loading(true))
                val response = api.addProducts(product)
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response))
                    emit(Resource.Loading(false))
                } else {
                    emit(Resource.Error(message = response.message))

                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: Constants.UPLOAD_ERROR))
            }
        }.flowOn(dispatcher)


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


}
