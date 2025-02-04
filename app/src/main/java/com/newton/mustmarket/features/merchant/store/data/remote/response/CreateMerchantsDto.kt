package com.newton.mustmarket.features.merchant.store.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMerchantsDto(
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: CreateMerchantsData
)
