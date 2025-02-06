package com.newton.mustmarket.features.merchant.create_store.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.create_store.data.remote.response.MerchantResponse
import com.newton.mustmarket.features.merchant.create_store.domain.models.CreateMerchantRequest
import kotlinx.coroutines.flow.Flow

interface MerchantRepository {
    suspend fun addMerchant(addMerchantRequest: CreateMerchantRequest): Flow<Resource<MerchantResponse>>
}