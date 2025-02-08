package com.newton.mustmarket.features.merchant.get_merchants.domain.model

import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.get_products.domain.model.products.NetworkProduct
import kotlinx.serialization.Serializable

@Serializable
data class GetMerchantsData(
    val id: Int,
    val name: String,
    val user: AuthedUser,
    val banner: String,
    val address: String,
    val profile: String,
    val description: String,
    val product: List<NetworkProduct>? = emptyList(),
    val reviews: List<String>? = emptyList(),
    val createdAt: String,
    val updatedAt: String? = null,
)
