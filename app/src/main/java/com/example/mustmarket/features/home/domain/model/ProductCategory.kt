package com.example.mustmarket.features.home.domain.model

import kotlinx.serialization.Serializable
@Serializable
data class ApiCategoryResponse(
    val message: String,
    val data: List<ProductCategory>,
)


@Serializable
data class ProductCategory(
    val id: String,
    val name: String,
//    val categoryImage: String,
)
