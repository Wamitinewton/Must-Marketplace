package com.newton.mustmarket.features.products.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CategoryResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<CategoryDto>
)
