package com.example.mustmarket.features.merchant.store.domain

import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
import kotlinx.serialization.Serializable


@Serializable
data class CreateMerchantResponse(
    val message: String,
    val data: MerchantResponseData
)


@Serializable
data class MerchantResponseData(
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
