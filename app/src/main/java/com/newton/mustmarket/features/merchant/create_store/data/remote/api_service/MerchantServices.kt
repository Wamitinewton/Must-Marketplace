package com.newton.mustmarket.features.merchant.create_store.data.remote.api_service

import com.newton.mustmarket.features.merchant.create_store.data.remote.response.MerchantResponse
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MerchantServices {
    @POST(MerchantEndpoints.ADD_MERCHANT)
    suspend fun addMerchant(
        @Body addMerchant: CreateMerchantRequest
    ): MerchantResponse
}