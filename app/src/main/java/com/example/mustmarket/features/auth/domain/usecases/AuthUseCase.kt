package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.domain.model.SignUpResult
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val repository: AuthRepository) {
    suspend  fun loginUseCase(loginUser: LoginUser) =
        repository.loginUser(loginUser)

    suspend fun registerUseCase(signUpUser: SignUpUser): Flow<Resource<SignUpResult>> =
        repository.signUp(signUpUser)

    suspend fun tokenUseCase(token: String) = repository.login(token)
}