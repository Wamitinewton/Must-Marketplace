package com.example.mustmarket.features.auth.data.remote

import com.example.mustmarket.features.auth.data.dto.LogInResultDto
<<<<<<< HEAD
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.data.dto.User
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
=======
import com.example.mustmarket.features.auth.data.dto.User
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RefreshToken
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun signUpUser(@Body signUp: SignUpUser): User

    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body loginCredentials: LoginRequest): LogInResultDto

    @POST("api/v1/auth/request-password-reset")
    suspend fun requestOtp(@Body request: RequestPasswordReset): OtpResponse

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body otpRequest: OtpRequest): PasswordResetResponse

    @POST("api/v1/auth/refresh-token")
<<<<<<< HEAD
    suspend fun refreshToken(@Body refreshTokenRequest: String?): LoginResult
=======
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshToken): User
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

}

