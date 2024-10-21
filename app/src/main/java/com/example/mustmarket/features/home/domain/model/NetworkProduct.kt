package com.example.mustmarket.features.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    val id: Int,
    val name: String,
    val imageUrl: List<String>,
    val description: String,
    val price: Double,
    val category: ProductCategory,
    val inventory: ProductInventory
)
