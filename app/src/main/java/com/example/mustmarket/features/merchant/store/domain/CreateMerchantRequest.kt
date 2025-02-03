package com.example.mustmarket.features.merchant.store.domain

import kotlinx.serialization.Serializable

@Serializable
data class CreateMerchantRequest(
    val name: String,
    val location: String,
    val userId: String,
    val banner: String,
    val profile: String,
    val description: String
)
