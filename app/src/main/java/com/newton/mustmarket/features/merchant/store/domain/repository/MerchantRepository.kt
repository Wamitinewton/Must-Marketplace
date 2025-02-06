package com.newton.mustmarket.features.merchant.store.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.store.domain.models.MerchantResponse
import kotlinx.coroutines.flow.Flow

interface MerchantRepository {
    suspend fun addMerchant(addMerchantRequest: CreateMerchantRequest): Flow<Resource<MerchantResponse>>
}