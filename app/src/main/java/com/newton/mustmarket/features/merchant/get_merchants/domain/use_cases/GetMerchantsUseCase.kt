package com.newton.mustmarket.features.merchant.get_merchants.domain.use_cases

import com.newton.mustmarket.features.merchant.get_merchants.domain.repository.GetMerchantsRepository

class GetMerchantsUseCase(
    private val getMerchantsRepository: GetMerchantsRepository
) {
    suspend fun getAllMerchants() = getMerchantsRepository.getAllMerchants()

    suspend fun getMerchantById(id: Int) = getMerchantsRepository.getMerchantById(id)
}