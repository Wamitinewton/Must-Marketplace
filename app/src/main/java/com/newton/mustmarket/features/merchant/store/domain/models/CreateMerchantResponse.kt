package com.newton.mustmarket.features.merchant.store.domain.models

import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.serialization.Serializable


@Serializable
data class CreateMerchantResponse(
    val message: String,
    val data: MerchantResponse
)


@Serializable
data class MerchantResponse(
    val storeId: String,
    val name: String,
    val user: AuthedUser,
    val banner: String,
    val location: String,
    val profile: String,
    val description: String,
    val products: List<NetworkProduct>? = null,
    val reviews: List<String>? = null,
    val createdAt: String,
    val updatedAt: String? = null
)
