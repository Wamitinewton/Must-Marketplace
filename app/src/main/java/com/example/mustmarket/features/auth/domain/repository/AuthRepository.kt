package com.example.mustmarket.features.auth.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.domain.model.AuthedUser
<<<<<<< HEAD
import com.example.mustmarket.features.auth.domain.model.LoginData
=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RefreshToken
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(signUp: SignUpUser): Flow<Resource<AuthedUser>>
    suspend fun loginUser(loginCredentials: LoginRequest): Flow<Resource<LoginResult>>
    suspend fun requestOtp(email: RequestPasswordReset): Flow<Resource<OtpResponse>>
    suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<PasswordResetResponse>>
<<<<<<< HEAD
    suspend fun refreshTokenFromServer(): LoginResult?
    suspend fun storeAuthTokens(accessToken: String, refreshToken: String)
    suspend fun getLoggedInUser(): AuthedUser?
    suspend fun storeLoggedInUser(user: AuthedUser)
     fun getAccessToken(): String?
     fun getRefreshToken(): String?
=======
    suspend fun refreshTokenFromServer(refreshToken: RefreshToken): Flow<Resource<AuthedUser>>
    suspend fun storeAuthTokens(accessToken: String, refreshToken: String)
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
}