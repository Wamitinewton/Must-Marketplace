package com.example.mustmarket.features.merchant.products.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UploadProductRequest(
    val userId: String,
    val brand: String?,
    val category: String?,
    val description: String?,
    val images: List<String>?,
    val inventory: Int?,
    val name: String?,
    val price: Int?
)