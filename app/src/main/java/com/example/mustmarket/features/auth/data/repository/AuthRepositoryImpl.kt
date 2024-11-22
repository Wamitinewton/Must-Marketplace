package com.example.mustmarket.features.auth.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.datastore.SessionManager
import com.example.mustmarket.features.auth.datastore.UserData
import com.example.mustmarket.features.auth.datastore.UserStoreManager
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.mapper.toAuthedUser
import com.example.mustmarket.features.auth.mapper.toLoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException as RetrofitHttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManger: SessionManager,
    private val userStoreManager: UserStoreManager
) : AuthRepository {
    override suspend fun signUp(signUp: SignUpUser): Flow<Resource<AuthedUser>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = authApi.signUpUser(signUp)
            if (response.message == "Success") {
                val user = response.data.toAuthedUser()
                emit(Resource.Success(data = user))
                emit(Resource.Loading(false))
            } else {
                emit(Resource.Error(response.data.toString()))
            }
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message.toString()
                )
            )
        }
        emit(Resource.Loading(false))
    }

    override suspend fun loginUser(loginCredentials: LoginRequest): Flow<Resource<LoginResult>> =
        flow {
            try {
                emit(Resource.Loading(true))
                val response = authApi.loginUser(loginCredentials)
                emit(Resource.Success(data = response.toLoginResult()))
                sessionManger.saveTokens(response.data.token, response.data.refreshToken)
                userStoreManager.saveUserData(
                    UserData(
                        id = response.data.user.id.toString(),
                        name = response.data.user.name,
                        email = response.data.user.email,
                        lastLoginTimeStamp = System.currentTimeMillis()
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Couldn't reach server, check your internet connection",
                    )
                )
            }
            emit(Resource.Loading(false))
        }

    override suspend fun logout(): Flow<Resource<Boolean>> {
        val isValid = sessionManger.isSessionValid()
        if (!isValid) {
            sessionManger.clearTokens()
            userStoreManager.clearUserData()
        }
        return flow {
            emit(Resource.Success(data = isValid))
        }
    }

    override suspend fun requestOtp(email: String): Flow<Resource<OtpResponse>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = authApi.requestOtp(email)
            emit(Resource.Success(data = response))
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
            emit(Resource.Error(message = errorMessage))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.message ?: "An unexpected error occurred"
                )
            )
        }
        emit(Resource.Loading(false))
    }

    override suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<PasswordResetResponse>> =
        flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.resetPassword(otpRequest)
                emit(Resource.Success(data = response))
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
                emit(Resource.Error(message = errorMessage))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                )
            }
            emit(Resource.Loading(false))
        }

}