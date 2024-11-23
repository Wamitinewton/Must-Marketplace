package com.example.mustmarket.features.products.domain.models

import com.example.mustmarket.features.products.data.remote.uploadResponse.Category
import kotlinx.serialization.Serializable

@Serializable
data class UploadData(
    val brand: String = "",
    val category: Category = Category(
        id = 0,
        image = "",
        name ="",
    ),
    val description: String="",
    val id: Int = 0,
    val images: List<String> = emptyList(),
    val inventory: Int = 0,
    val name: String = "",
    val price: Int = 0
)
