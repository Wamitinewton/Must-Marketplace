package com.example.mustmarket.features.auth.data.remote.service

import com.example.mustmarket.features.auth.data.remote.auth_response.LogInResultDto
import com.example.mustmarket.features.auth.data.remote.auth_response.OtpResponse
import com.example.mustmarket.features.auth.data.remote.auth_response.PasswordResetResponse
import com.example.mustmarket.features.auth.data.remote.auth_response.User
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST(AuthorizationEndpoints.REGISTER)
    suspend fun signUpUser(@Body signUp: SignUpUser): User

    @POST(AuthorizationEndpoints.LOGIN)
    suspend fun loginUser(@Body loginCredentials: LoginRequest): LogInResultDto

    @POST(AuthorizationEndpoints.REQUEST_OTP)
    suspend fun requestOtp(@Body request: RequestPasswordReset): OtpResponse

    @POST(AuthorizationEndpoints.RESET_PASSWORD)
    suspend fun resetPassword(@Body otpRequest: OtpRequest): PasswordResetResponse

    @POST(AuthorizationEndpoints.REFRESH_TOKEN)
    suspend fun refreshToken(@Body refreshTokenRequest: String?): LoginResult

}

