package com.example.mustmarket.features.product.domain.model

data class Product(
    val brand: String,
    val category: Category,
    val description: String,
    val id: Int,
    val images: List<Image>,
    val inventory: Int,
    val name: String,
    val price: Int
)