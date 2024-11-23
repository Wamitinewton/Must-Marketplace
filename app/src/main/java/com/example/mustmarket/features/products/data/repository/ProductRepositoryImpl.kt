package com.example.mustmarket.features.products.data.repository

import com.example.mustmarket.core.util.Constants
import com.example.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.products.data.remote.UploadProductsApi
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: UploadProductsApi
) : ProductRepository {
    override suspend fun uploadListOfImages(images: List<File>): Flow<Resource<UploadImageListOfImageResponse>> = flow{
        try {
            emit(Resource.Loading(true))
            val response = api.uploadListOfImages(images)
            if (response.message == SUCCESS_RESPONSE){
                emit(Resource.Success(data = response))
                emit(Resource.Loading(false))
            }else{
                emit(Resource.Loading(false))
                emit(Resource.Error(message = response.data.toString()))

            }
        } catch (e: Exception) {
            emit(Resource.Loading(false))
            emit(Resource.Error(message = e.message?: Constants.UPLOAD_ERROR))
        }
    }

    override suspend fun uploadSingleImages(image: File): Flow<Resource<UploadSingleImageResponse>> = flow{
        try {
            emit(Resource.Loading(true))
            val response = api.uploadSingleImage(image)
            if (response.message == SUCCESS_RESPONSE){
                emit(Resource.Success(data = response))
                emit(Resource.Loading(false))
            }else{
                emit(Resource.Loading(false))
                emit(Resource.Error(message = response.data.toString()))

            }
        } catch (e: Exception) {
            emit(Resource.Loading(false))
            emit(Resource.Error(message = e.message?: Constants.UPLOAD_ERROR))
        }
    }

    override suspend fun addProduct(product: UploadProductRequest): Flow<Resource<UploadProductResponse>> =flow{
        try {
            emit(Resource.Loading(true))
            val response = api.addProducts(product)
            if (response.message == SUCCESS_RESPONSE){
                emit(Resource.Success(data = response))
                emit(Resource.Loading(false))
            }else{
                emit(Resource.Loading(false))
                emit(Resource.Error(message = response.data.toString()))

            }
        } catch (e: Exception) {
            emit(Resource.Loading(false))
            emit(Resource.Error(message = e.message?: Constants.UPLOAD_ERROR))
        }
    }

}
