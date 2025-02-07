package com.newton.mustmarket.usecase

import com.newton.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.newton.mustmarket.features.products.domain.usecases.HomeUseCases
import com.newton.mustmarket.features.merchant.products.domain.usecases.AddProductUseCase
import com.newton.mustmarket.features.merchant.create_store.domain.usecases.MerchantUseCase

data class UseCases(
    val authUseCase: AuthUseCase,
    val homeUseCases: HomeUseCases,
    val addProduct: AddProductUseCase,
    val merchantUseCase: MerchantUseCase
)