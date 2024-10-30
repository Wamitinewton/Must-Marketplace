package com.example.mustmarket

import com.example.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.example.mustmarket.features.home.domain.usecases.HomeUseCases

data class UseCases(
    val authUseCase: AuthUseCase,
    val homeUseCases: HomeUseCases,
)