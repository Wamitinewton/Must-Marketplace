package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.features.auth.domain.repository.AuthRepository

class TokenSession(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(token: String) = repository.login(token)
}