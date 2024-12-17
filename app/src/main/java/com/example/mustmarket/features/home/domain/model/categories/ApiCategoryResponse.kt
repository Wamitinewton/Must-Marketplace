package com.example.mustmarket.features.home.domain.model.categories

import kotlinx.serialization.Serializable
@Serializable
data class ApiCategoryResponse(
    val message: String,
    val data: List<ProductCategory>,
)


