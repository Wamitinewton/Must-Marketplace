package com.newton.mustmarket.features.merchant.get_merchants.data.remote.api_response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMerchantsResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val dataDto: List<GetMerchantsDataDto>? = emptyList()
)
