package com.example.mustmarket.features.auth.domain.usecases

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.OtpRequest
<<<<<<< HEAD
import com.example.mustmarket.features.auth.domain.model.RefreshToken
=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val repository: AuthRepository) {
    suspend fun loginUseCase(loginUser: LoginRequest) =
        repository.loginUser(loginUser)

    suspend fun registerUseCase(signUpUser: SignUpUser): Flow<Resource<AuthedUser>> =
        repository.signUp(signUpUser)

    suspend fun requestOtpUseCase(email: RequestPasswordReset) = repository.requestOtp(email)

    suspend fun resetPasswordUseCase(otpRequest: OtpRequest) = repository.resetPassword(otpRequest)

<<<<<<< HEAD
    suspend fun refreshTokens(refreshToken: RefreshToken) = repository.refreshTokenFromServer()

    suspend fun storeTokens(accessToken: String, refreshToken: String) = repository.storeAuthTokens(accessToken, refreshToken)

    suspend fun getLoggedInUser() = repository.getLoggedInUser()

    suspend fun storeLoggedInUser(user: AuthedUser) = repository.storeLoggedInUser(user)

    fun getAccessToken() = repository.getAccessToken()

    fun getRefreshToken() = repository.getRefreshToken()


=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

}