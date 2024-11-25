package com.example.mustmarket.features.products.data.remote

import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadImageListOfImageResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.products.data.remote.uploadResponse.UploadSingleImageResponse
import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface UploadProductsApi {
    @POST("api/v1/products/add")
    suspend fun addProducts(
        @Body uploadProductRequest: UploadProductRequest
    ): UploadProductResponse

    @Multipart
    @POST("api/v1/images/uploadListImages")
    suspend fun uploadListOfImages(@Part files:List<MultipartBody.Part>): UploadImageListOfImageResponse

    @POST("api/v1/images/uploadSingleImage")
    suspend fun uploadSingleImage(file:File): UploadSingleImageResponse
}