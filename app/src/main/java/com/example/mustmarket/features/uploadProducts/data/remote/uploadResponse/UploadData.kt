package com.example.mustmarket.features.uploadProducts.data.remote.uploadResponse

import kotlinx.serialization.Serializable

@Serializable
data class UploadData(
    val brand: String,
    val category: Category,
    val description: String,
    val id: Int,
    val images: List<String>,
    val inventory: Int,
    val name: String,
    val price: Int
)
