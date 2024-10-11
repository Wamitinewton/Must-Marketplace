package com.example.mustmarket

import com.example.mustmarket.features.auth.domain.usecases.LoginUseCase
import com.example.mustmarket.features.auth.domain.usecases.SignUpUseCase
import com.example.mustmarket.features.auth.domain.usecases.TokenSession

data class UseCases(
    val signUpUseCase: SignUpUseCase,
    val loginUseCase: LoginUseCase,
    val tokenLogin: TokenSession
)