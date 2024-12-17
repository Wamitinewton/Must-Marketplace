package com.example.mustmarket.features.merchant.products.presentation.state

import com.example.mustmarket.features.merchant.products.domain.models.UploadData

data class UploadProductUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val uploadData: UploadData? = null,
    val uploadMultipleImagesState: UploadMultipleImageState = UploadMultipleImageState(),
    val uploadSingleImageState: UploadSingleImageUrlState = UploadSingleImageUrlState(),
    val productInput: UploadProductsInput = UploadProductsInput(),
)

data class UploadProductsInput(
    val productName: String = "",
    val productDescription: String = "",
    val productPrice: Int? = 0,
    val productInventory: Int? = 0,
    var productCategory: String = "",
    val productBrand: String = "",
    )

data class UploadMultipleImageState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val multipleImagesUrl: List<String>? = null,
)

data class UploadSingleImageUrlState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val singleImageUrl: String? = null,
)