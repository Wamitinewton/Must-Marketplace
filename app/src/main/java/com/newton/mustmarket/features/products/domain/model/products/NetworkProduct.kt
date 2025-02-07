package com.newton.mustmarket.features.products.domain.model.products

import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.products.domain.model.categories.ProductCategory
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
