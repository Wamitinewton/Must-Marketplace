package com.newton.mustmarket.features.merchant.upload_products.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.upload_products.data.remote.uploadResponse.UploadProductResponse
import com.newton.mustmarket.features.merchant.upload_products.domain.models.UploadProductRequest
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun addProduct(product: UploadProductRequest):  Flow<Resource<UploadProductResponse>>
}