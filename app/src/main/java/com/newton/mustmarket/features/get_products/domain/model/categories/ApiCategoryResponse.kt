package com.newton.mustmarket.features.get_products.domain.model.categories

import kotlinx.serialization.Serializable
@Serializable
data class ApiCategoryResponse(
    val message: String,
    val data: List<ProductCategory>,
)


