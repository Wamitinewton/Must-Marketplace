package com.newton.mustmarket.features.merchant.create_store.merchant_keystore

import kotlinx.coroutines.flow.Flow

interface MerchantPrefsRepository {
    suspend fun setMerchantStatus(isMerchant: Boolean)
    fun getMerchantStatus(): Flow<Boolean>
}