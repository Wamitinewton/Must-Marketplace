package com.newton.mustmarket.features.auth.domain.usecases

import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.model.LoginRequest
import com.newton.mustmarket.features.auth.domain.model.OtpRequest
import com.newton.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.newton.mustmarket.features.auth.domain.model.SignUpUser
import com.newton.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val repository: AuthRepository) {
    suspend fun loginUseCase(loginUser: LoginRequest) =
        repository.loginUser(loginUser)

    suspend fun registerUseCase(signUpUser: SignUpUser): Flow<Resource<AuthedUser>> =
        repository.signUp(signUpUser)

    suspend fun requestOtpUseCase(email: RequestPasswordReset) = repository.requestOtp(email)

    suspend fun resetPasswordUseCase(otpRequest: OtpRequest) = repository.resetPassword(otpRequest)

    suspend fun storeTokens(accessToken: String, refreshToken: String) = repository.storeAuthTokens(accessToken, refreshToken)

    suspend fun getLoggedInUser() = repository.getLoggedInUser()

    suspend fun storeLoggedInUser(user: AuthedUser) = repository.storeLoggedInUser(user)

    fun getAccessToken() = repository.getAccessToken()


}