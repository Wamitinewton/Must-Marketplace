package com.example.mustmarket.features.auth.data.remote

import com.example.mustmarket.features.auth.data.dto.LogInResultDto
import com.example.mustmarket.features.auth.data.dto.User
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun signUpUser(@Body signUp: SignUpUser): User

    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body loginCredentials: LoginRequest): LogInResultDto

    @POST("api/v1/auth/request-password-reset")
    suspend fun requestOtp(@Body email: String): OtpResponse

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body otpRequest: OtpRequest): PasswordResetResponse

}

