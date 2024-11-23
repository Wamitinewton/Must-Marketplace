package com.example.mustmarket.features.products.domain.usecases

import com.example.mustmarket.features.products.domain.repository.ProductRepository
import java.io.File
import javax.inject.Inject

class UploadImageListUseCase (
    private val repository: ProductRepository
){
    suspend operator fun invoke(images:List<File>)=repository.uploadListOfImages(images)
}