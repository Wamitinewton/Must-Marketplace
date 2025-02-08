package com.newton.mustmarket.features.auth.domain.repository

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.auth.data.remote.auth_response.OtpResponse
import com.newton.mustmarket.features.auth.data.remote.auth_response.PasswordResetResponse
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.model.LoginRequest
import com.newton.mustmarket.features.auth.domain.model.LoginResult
import com.newton.mustmarket.features.auth.domain.model.OtpRequest
import com.newton.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.newton.mustmarket.features.auth.domain.model.SignUpUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(signUp: SignUpUser): Flow<Resource<AuthedUser>>
    suspend fun loginUser(loginCredentials: LoginRequest): Flow<Resource<LoginResult>>
    suspend fun requestOtp(email: RequestPasswordReset): Flow<Resource<OtpResponse>>
    suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<PasswordResetResponse>>
    suspend fun refreshTokenFromServer(): LoginResult?
    suspend fun storeAuthTokens(accessToken: String, refreshToken: String)
    suspend fun updateAuthTokens(accessToken: String, refreshToken: String)
    suspend fun getLoggedInUser(): AuthedUser?
    suspend fun storeLoggedInUser(user: AuthedUser)
     fun getAccessToken(): String?
     fun getRefreshToken(): String?
}