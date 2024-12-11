package com.example.mustmarket.features.merchant.products.domain.models

import com.example.mustmarket.features.home.domain.model.categories.ProductCategory
import kotlinx.serialization.Serializable


@Serializable
data class UploadData(
    val brand: String,
    val category: ProductCategory,
    val description: String,
    val id: Int,
    val images: List<String>,
    val inventory: Int,
    val name: String,
    val price: Int,
    val user: UserData?
)


@Serializable
data class UserData(
    val email: String,
    val id: Int,
    val name: String,
    val number: Int? = null
)
