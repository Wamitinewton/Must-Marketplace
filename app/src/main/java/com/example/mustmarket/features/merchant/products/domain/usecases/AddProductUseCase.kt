package com.example.mustmarket.features.merchant.products.domain.usecases

import com.example.mustmarket.features.merchant.products.domain.models.UploadProductRequest
import com.example.mustmarket.features.merchant.products.domain.repository.ProductRepository
import java.io.File
import javax.inject.Inject

class AddProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun addProduct(product: UploadProductRequest) = repository.addProduct(product)
    suspend fun uploadListOfImages(images: List<File>) = repository.uploadListOfImages(images)
    suspend fun uploadSingleImages(image: File) = repository.uploadSingleImages(image)
}