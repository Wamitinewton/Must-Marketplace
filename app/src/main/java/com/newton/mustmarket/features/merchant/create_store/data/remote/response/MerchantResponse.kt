package com.newton.mustmarket.features.merchant.create_store.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MerchantResponse(
    val message: String,
    val data: MerchantResponseData
)
