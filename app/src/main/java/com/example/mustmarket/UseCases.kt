package com.example.mustmarket

import com.example.mustmarket.features.auth.domain.usecases.LoginUseCase
import com.example.mustmarket.features.auth.domain.usecases.SignUpUseCase

data class UseCases(
    val signUpUseCase: SignUpUseCase,
    val loginUseCase: LoginUseCase
)