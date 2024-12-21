package com.example.mustmarket.features.merchant.products.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.example.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.example.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProductRepository {
    suspend fun uploadListOfImages(images: List<File>): Flow<Resource<UploadImageListOfImageResponse>>
    suspend fun uploadSingleImages(images: File): Flow<Resource<UploadSingleImageResponse>>
    suspend fun addProduct(product: UploadProductRequest):  Flow<Resource<UploadProductResponse>>
}