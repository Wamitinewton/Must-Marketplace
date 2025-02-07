package com.newton.mustmarket.features.merchant.create_store.domain.models

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
