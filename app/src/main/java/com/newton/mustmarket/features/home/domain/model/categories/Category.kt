package com.newton.mustmarket.features.home.domain.model.categories

import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory(
    val id: Int,
    val name: String,
    val categoryImage: String,
)

