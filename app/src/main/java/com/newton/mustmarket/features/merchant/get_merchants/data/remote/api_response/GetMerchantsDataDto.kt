package com.newton.mustmarket.features.merchant.get_merchants.data.remote.api_response

import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.get_products.data.remote.response.NetworkProductDto
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMerchantsDataDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("user")
    val user: UserDto,
    @SerialName("banner")
    val banner: String,
    @SerialName("address")
    val address: String,
    @SerialName("profile")
    val profile: String,
    @SerialName("description")
    val description: String,
    @SerialName("product")
    val product: List<NetworkProductDto>? = emptyList(),
    @SerialName("reviews")
    val reviews: List<String>? = emptyList(),
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
)
