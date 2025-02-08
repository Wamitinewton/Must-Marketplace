package com.newton.mustmarket.features.merchant.get_merchants.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsData
import com.newton.mustmarket.features.merchant.get_merchants.domain.model.GetMerchantsResponse
import kotlinx.coroutines.flow.Flow

interface GetMerchantsRepository {
    suspend fun getAllMerchants(): Flow<Resource<List<GetMerchantsData>>>
}