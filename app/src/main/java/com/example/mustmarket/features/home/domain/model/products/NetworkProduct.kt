package com.example.mustmarket.features.home.domain.model.products

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
<<<<<<< HEAD
    val imageUrl: List<String> = emptyList()
=======
    val images: List<String> = emptyList<String>()
>>>>>>> fc9add0a846ee8f951afeb7b6115901d7b12286c
)
