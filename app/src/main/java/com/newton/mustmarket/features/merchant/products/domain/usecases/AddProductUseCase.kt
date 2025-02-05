package com.newton.mustmarket.features.merchant.products.domain.usecases

import com.newton.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import com.newton.mustmarket.features.merchant.products.domain.repository.ProductRepository
import java.io.File

class AddProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun addProduct(product: UploadProductRequest) = repository.addProduct(product)
}