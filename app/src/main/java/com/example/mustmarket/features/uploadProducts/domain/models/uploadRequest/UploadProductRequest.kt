package com.example.mustmarket.features.uploadProducts.domain.models.uploadRequest

import kotlinx.serialization.Serializable

@Serializable
data class UploadProductRequest(
    val brand: String,
    val category: String,
    val description: String,
    val images: List<String>,
    val inventory: String,
    val name: String,
    val price: String
)