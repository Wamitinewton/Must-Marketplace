package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val repository: AuthRepository) {
    suspend fun loginUseCase(loginUser: LoginRequest) =
        repository.loginUser(loginUser)

    suspend fun registerUseCase(signUpUser: SignUpUser): Flow<Resource<AuthedUser>> =
        repository.signUp(signUpUser)

    suspend fun logoutUseCase() = repository.logout()

    suspend fun requestOtpUseCase(email: String) = repository.requestOtp(email)

    suspend fun resetPasswordUseCase(otpRequest: OtpRequest) = repository.resetPassword(otpRequest)


}