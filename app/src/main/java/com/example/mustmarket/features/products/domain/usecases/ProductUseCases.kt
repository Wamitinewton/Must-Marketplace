package com.example.mustmarket.features.products.domain.usecases

data class ProductUseCases(
    val uploadImage: UploadSingleImageUseCase,
    val uploadImageList: UploadImageListUseCase,
    val addProduct: AddProductUseCase
)
