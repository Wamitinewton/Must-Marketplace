package com.example.mustmarket.features.products.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.products.domain.models.UploadData
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProductRepository {
    suspend fun uploadListOfImages(images: List<File>): Flow<Resource<UploadImageListOfImageResponse>>
    suspend fun uploadSingleImages(images: File): Flow<Resource<UploadSingleImageResponse>>
    suspend fun addProduct(product:UploadProductRequest):  Flow<Resource<UploadProductResponse>>
}