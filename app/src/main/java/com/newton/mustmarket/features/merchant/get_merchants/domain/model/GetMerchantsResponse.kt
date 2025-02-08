package com.newton.mustmarket.features.merchant.get_merchants.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GetMerchantsResponse(
    val message: String,
    val data: List<GetMerchantsData>? = emptyList()
)
