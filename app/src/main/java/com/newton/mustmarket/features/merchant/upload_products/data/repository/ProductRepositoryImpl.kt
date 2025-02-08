package com.newton.mustmarket.features.merchant.upload_products.data.repository

import com.newton.mustmarket.core.util.Constants
import com.newton.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.di.IODispatcher
import com.newton.mustmarket.features.merchant.upload_products.data.remote.UploadProductsApi
import com.newton.mustmarket.features.merchant.upload_products.data.remote.uploadResponse.UploadProductResponse
import com.newton.mustmarket.features.merchant.upload_products.domain.models.UploadProductRequest
import com.newton.mustmarket.features.merchant.upload_products.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: UploadProductsApi,
    @IODispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductRepository {


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


}
