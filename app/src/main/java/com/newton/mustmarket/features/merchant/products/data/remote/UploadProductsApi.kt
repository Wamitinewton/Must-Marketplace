package com.newton.mustmarket.features.merchant.products.data.remote

import com.newton.mustmarket.features.merchant.products.data.remote.uploadResponse.UploadProductResponse
import com.newton.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadProductsApi {
    @POST("api/v1/products/add")
    suspend fun addProducts(
        @Body uploadProductRequest: UploadProductRequest
    ): UploadProductResponse
}