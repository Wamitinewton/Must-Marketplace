package com.example.mustmarket.features.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory(
    val id: Int,
    val name: String,
//    val categoryImage: String,
)

