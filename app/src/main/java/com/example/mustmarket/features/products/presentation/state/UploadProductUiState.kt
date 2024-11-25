package com.example.mustmarket.features.products.presentation.state

import com.example.mustmarket.features.products.domain.models.UploadData

data class UploadProductUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val singleImageUrl: String = "",
    val imageUrls: List<String> = emptyList(),
    val uploadData: UploadData? = null
)