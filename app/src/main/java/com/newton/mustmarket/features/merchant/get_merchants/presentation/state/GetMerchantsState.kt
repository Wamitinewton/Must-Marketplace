package com.newton.mustmarket.features.merchant.get_merchants.presentation.state

import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData

data class GetMerchantsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: List<GetMerchantsData>? = emptyList(),
)

data class GetMerchantByIdState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: GetMerchantsData? = null,
)

