package com.example.mustmarket.features.products.domain.usecases

import com.example.mustmarket.features.products.domain.repository.ProductRepository
import java.io.File
import javax.inject.Inject

class UploadSingleImageUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(image: File) = repository.uploadSingleImages(image)
}