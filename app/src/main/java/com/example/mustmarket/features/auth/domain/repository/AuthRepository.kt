package com.example.mustmarket.features.auth.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(signUp: SignUpUser): Flow<Resource<AuthedUser>>
    suspend fun loginUser(loginCredentials: LoginRequest): Flow<Resource<LoginResult>>
    suspend fun logout(): Flow<Resource<Boolean>>
    suspend fun requestOtp(email: RequestPasswordReset): Flow<Resource<OtpResponse>>
    suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<PasswordResetResponse>>
}