package com.newton.mustmarket.features.merchant.create_store.data.remote.response

import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import com.newton.mustmarket.features.products.data.remote.response.NetworkProductDto
import kotlinx.serialization.Serializable

@Serializable
data class MerchantResponseData(
    val id: String,
    val name: String,
    val userDto: UserDto,
    val banner: String,
    val address: String,
    val profile: String,
    val description: String,
    val products: List<NetworkProductDto>? = null,
    val reviews: List<String>? = null,
    val createdAt: String,
    val updatedAt: String? = null
)