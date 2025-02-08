package com.newton.mustmarket.features.merchant.create_store.data.remote.api_service

import com.newton.mustmarket.features.merchant.create_store.data.remote.response.MerchantResponse
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.get_merchants.data.remote.api_response.GetMerchantsDataDto
import com.newton.mustmarket.features.merchant.get_merchants.data.remote.api_response.GetMerchantsResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MerchantServices {
    @POST(MerchantEndpoints.ADD_MERCHANT)
    suspend fun addMerchant(
        @Body addMerchant: CreateMerchantRequest
    ): MerchantResponse

    @GET(MerchantEndpoints.GET_ALL_MERCHANTS)
    suspend fun getAllMerchants(): GetMerchantsResponseDto

    @GET(MerchantEndpoints.GET_MERCHANT_BY_ID)
    suspend fun getMerchantById(
        @Path("id") merchantId: Int
    ): GetMerchantsDataDto
}