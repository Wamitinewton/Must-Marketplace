package com.example.mustmarket.features.home.domain.model

import com.example.mustmarket.features.home.data.remote.dto.CategoryDto
import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory(
    val id: Int,
    val name: String,
    val categoryImage: String,
)

