package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.SignUpResult
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(signUpUser: SignUpUser): Flow<Resource<SignUpResult>> = repository.signUp(signUpUser)
}