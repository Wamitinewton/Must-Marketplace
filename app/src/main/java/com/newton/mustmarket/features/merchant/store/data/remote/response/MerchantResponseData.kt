package com.newton.mustmarket.features.merchant.store.data.remote.response

import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import com.newton.mustmarket.features.home.data.remote.response.NetworkProductDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantResponseData(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("user")
    val userDto: UserDto,
    @SerialName("banner")
    val banner: String,
    @SerialName("address")
    val address: String,
    @SerialName("profile")
    val profile: String,
    @SerialName("description")
    val description: String,
    @SerialName("products")
    val products: List<NetworkProductDto>? = null,
    @SerialName("reviews")
    val reviews: List<String>? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String? = null
)