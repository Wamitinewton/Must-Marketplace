package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(loginUser: LoginUser) =
        repository.loginUser(loginUser)
}