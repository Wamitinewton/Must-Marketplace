package com.newton.mustmarket.features.get_products.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllProductsDto(
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<NetworkProductDto>
)

@Serializable
data class ProductDetailsDto(
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: NetworkProductDto
)

