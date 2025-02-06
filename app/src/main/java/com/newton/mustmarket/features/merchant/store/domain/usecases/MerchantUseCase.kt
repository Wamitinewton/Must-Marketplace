package com.newton.mustmarket.features.merchant.store.domain.usecases

import com.newton.mustmarket.features.merchant.store.domain.models.CreateMerchantRequest
import com.newton.mustmarket.features.merchant.store.domain.repository.MerchantRepository

class MerchantUseCase(
    private val merchantRepository: MerchantRepository
) {
    suspend fun addMerchant(createMerchantRequest: CreateMerchantRequest) = merchantRepository.addMerchant(createMerchantRequest)
}