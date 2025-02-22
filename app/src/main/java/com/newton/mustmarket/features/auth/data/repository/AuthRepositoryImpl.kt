package com.newton.mustmarket.features.auth.data.repository

import coil.network.HttpException
import com.newton.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.newton.mustmarket.core.util.Resource
import com.newton.mustmarket.database.dao.UserDao
import com.newton.mustmarket.database.mappers.toAuthedUser
import com.newton.mustmarket.database.mappers.toUserEntity
import com.newton.mustmarket.features.auth.data.auth_mappers.toAuthedUser
import com.newton.mustmarket.features.auth.data.auth_mappers.toLoginResult
import com.newton.mustmarket.features.auth.data.datastore.SessionManager
import com.newton.mustmarket.features.auth.data.remote.service.AuthenticationService
import com.newton.mustmarket.features.auth.data.tokenHolder.AuthTokenHolder
import com.newton.mustmarket.features.auth.domain.model.AuthedUser
import com.newton.mustmarket.features.auth.domain.model.LoginRequest
import com.newton.mustmarket.features.auth.domain.model.LoginResult
import com.newton.mustmarket.features.auth.domain.model.OtpRequest
import com.newton.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.newton.mustmarket.features.auth.domain.model.SignUpUser
import com.newton.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException as RetrofitHttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthenticationService,
    private val sessionManger: SessionManager,
    private val userDao: UserDao
) : AuthRepository {

    init {
        runBlocking {
            AuthTokenHolder.initializeTokens(sessionManger)
            Timber.d("Successfully initialized")
        }
    }


    override suspend fun signUp(signUp: SignUpUser): Flow<Resource<AuthedUser>> = flow {

        try {
            emit(Resource.Loading(true))
            val response = authApi.signUpUser(signUp)
            if (response.message == "Success") {
                val user = response.data.toAuthedUser()
                emit(Resource.Success(data = user))
                emit(Resource.Loading(false))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message ?: "Could not reach server, check your internet connection",
                )
            )
        }

    }

    override suspend fun loginUser(loginCredentials: LoginRequest): Flow<Resource<LoginResult>> =
        flow {
            try {
                emit(Resource.Loading(true))
                val response = authApi.loginUser(loginCredentials)
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response.toLoginResult()))
                } else {
                    emit(Resource.Error(response.message))
                }
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.message
                            ?: "Could not reach server, check your internet connection",
                    )
                )
            } catch (e: HttpException) {
                when(e.response.code) {
                    502 -> {
                        emit(Resource.Error(message = e.message ?: "Server temporarily unavailable. Try again later"))
                    } else -> emit(Resource.Error("Http error: ${e.response.code}"))
                }
            } finally {
                emit(Resource.Loading(false))

            }

        }


    override suspend fun requestOtp(email: RequestPasswordReset): Flow<Resource<com.newton.mustmarket.features.auth.data.remote.auth_response.OtpResponse>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.requestOtp(email)
                emit(Resource.Success(data = response))
                emit(Resource.Loading(false))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                )
            } catch (e: RetrofitHttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                } catch (e: Exception) {
                    "An unexpected error occurred"
                }
                emit(Resource.Error(message = errorMessage.toString()))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }

    override suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<com.newton.mustmarket.features.auth.data.remote.auth_response.PasswordResetResponse>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.resetPassword(otpRequest)
                emit(Resource.Success(data = response))
                emit(Resource.Loading(false))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Couldn't reach server, check your internet connection"
                    )
                )
            } catch (e: RetrofitHttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                } catch (e: Exception) {
                    "An unexpected error occurred"
                }
                emit(Resource.Error(message = errorMessage.toString()))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }

    override suspend fun refreshTokenFromServer(): LoginResult? {
        return try {
            val refreshToken = getRefreshToken()
            val response = authApi.refreshToken(refreshToken)
            response
        } catch (e: IOException) {
            Timber.e(message = "Failed to get refresh token")
            null
        } catch (e: HttpException) {
            Timber.e(message = "Connection Failed!")
            null
        }
    }


    override suspend fun storeAuthTokens(accessToken: String, refreshToken: String) {
        AuthTokenHolder.accessToken = accessToken
        AuthTokenHolder.refreshToken = refreshToken
        sessionManger.saveTokens(
            accessToken,
            refreshToken
        )

        // Debug: Check token persistence
        val savedAccessToken = sessionManger.fetchAccessToken()
        val savedRefreshToken = sessionManger.fetchRefreshToken()
        Timber.d("Tokens saved successfully: AccessToken=$savedAccessToken, RefreshToken=$savedRefreshToken")
    }

    override suspend fun updateAuthTokens(accessToken: String, refreshToken: String) {
        AuthTokenHolder.accessToken = accessToken
        AuthTokenHolder.refreshToken = refreshToken
        sessionManger.updateTokens(
            accessToken,
            refreshToken
        )
    }

    override suspend fun getLoggedInUser(): AuthedUser? {
        return userDao.getLoggedInUser()?.toAuthedUser()
    }

    override suspend fun storeLoggedInUser(user: AuthedUser) {
        return userDao.insertUser(user.toUserEntity())
    }

    override fun getAccessToken(): String? {
        return AuthTokenHolder.accessToken
    }

    override fun getRefreshToken(): String? {
        return AuthTokenHolder.refreshToken
    }

}