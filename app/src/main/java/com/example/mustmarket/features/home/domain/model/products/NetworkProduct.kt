package com.example.mustmarket.features.home.domain.model.products

import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    val id: Int,
    val name: String,
    val price: Double,
    val inventory: Int,
    val brand: String,
    val description: String,
    val category: ProductCategory,
    val images: List<String> = emptyList(),
    val userData: AuthedUser
)
