package com.example.mustmarket.features.uploadProducts.data.remote

import com.example.mustmarket.features.uploadProducts.data.remote.uploadResponse.UploadProductResponse
import com.example.mustmarket.features.uploadProducts.domain.models.uploadRequest.UploadProductRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadProductsApi {
    @POST("api/v1/products/add")
    suspend fun addProducts(
        @Body uploadProductRequest: UploadProductRequest
    ): UploadProductResponse
}