package com.newton.mustmarket.features.get_products.domain.model.categories

import kotlinx.serialization.Serializable

@Serializable
data class UploadCategoryResponse(
    val data: CategoryResponseData,
    val message: String
)

@Serializable
data class CategoryResponseData(
    val id: Int,
    val image: String,
    val name: String
)