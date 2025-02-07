package com.newton.mustmarket.features.products.data.remote.response

import com.newton.mustmarket.features.auth.data.remote.auth_response.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String? = null,

    @SerialName("price")
    val price: Double,

    @SerialName("inventory")
    val inventory: Int,

    @SerialName("brand")
    val brand: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("category")
    val category: CategoryDto,

    @SerialName("images")
    val images: List<String> = emptyList(),

    @SerialName("user")
    val user: UserDto? = null
)
