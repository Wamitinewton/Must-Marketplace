package com.example.mustmarket.features.products.presentation.state

import com.example.mustmarket.features.products.domain.models.UploadData

data class UploadProductUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val uploadData: UploadData? = null,
    val uploadMultipleImagesState: UploadMultipleImageState = UploadMultipleImageState(),
    val uploadSingleImageState: UploadSingleImageUrlState = UploadSingleImageUrlState()
)

data class UploadMultipleImageState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageUrls: List<String> = emptyList(),
)

data class UploadSingleImageUrlState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val singleImageUrl: String = "",
)