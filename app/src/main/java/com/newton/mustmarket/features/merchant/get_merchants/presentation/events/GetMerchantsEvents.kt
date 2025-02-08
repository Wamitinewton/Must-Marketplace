package com.newton.mustmarket.features.merchant.get_merchants.presentation.events

sealed class GetMerchantsEvents {
    data object GetAllMerchants: GetMerchantsEvents()
    data class GetMerchantById(val id: Int): GetMerchantsEvents()
}