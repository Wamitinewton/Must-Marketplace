package com.newton.mustmarket.usecase

import com.newton.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.newton.mustmarket.features.home.domain.usecases.HomeUseCases
import com.newton.mustmarket.features.merchant.products.domain.usecases.AddProductUseCase

data class UseCases(
    val authUseCase: AuthUseCase,
    val homeUseCases: HomeUseCases,
    val addProduct: AddProductUseCase
)