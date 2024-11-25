package com.example.mustmarket.features.products.domain.usecases

import com.example.mustmarket.features.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.products.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase(
    private val repository: ProductRepository
){
    suspend operator fun invoke(product: UploadProductRequest)= repository.addProduct(product)
}