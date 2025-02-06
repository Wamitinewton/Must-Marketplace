package com.newton.mustmarket.features.merchant.store.data.remote.api_service

import com.newton.mustmarket.features.merchant.store.data.remote.response.MerchantResponseDto
import com.newton.mustmarket.features.merchant.store.domain.models.CreateMerchantRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MerchantServices {
    @POST(MerchantEndpoints.ADD_MERCHANT)
    suspend fun addMerchant(
        @Body addMerchant: CreateMerchantRequest
    ): MerchantResponseDto
}