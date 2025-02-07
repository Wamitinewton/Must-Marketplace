package com.newton.mustmarket.features.merchant.upload_products.domain.usecases

import com.newton.mustmarket.features.merchant.upload_products.domain.models.UploadProductRequest
import com.newton.mustmarket.features.merchant.upload_products.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun addProduct(product: UploadProductRequest) = repository.addProduct(product)
}