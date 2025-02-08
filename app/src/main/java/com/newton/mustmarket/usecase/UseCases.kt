package com.newton.mustmarket.usecase

import com.newton.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.newton.mustmarket.features.get_products.domain.usecases.HomeUseCases
import com.newton.mustmarket.features.merchant.upload_products.domain.usecases.AddProductUseCase
import com.newton.mustmarket.features.merchant.create_store.domain.usecases.MerchantUseCase
import com.newton.mustmarket.features.merchant.get_merchants.domain.use_cases.GetMerchantsUseCase

data class UseCases(
    val authUseCase: AuthUseCase,
    val homeUseCases: HomeUseCases,
    val addProduct: AddProductUseCase,
    val merchantUseCase: MerchantUseCase,
    val getMerchantsUseCase: GetMerchantsUseCase
)