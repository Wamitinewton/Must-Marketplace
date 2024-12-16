package com.example.mustmarket.features.auth.data.repository

import com.example.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.datastore.SessionManager
import com.example.mustmarket.features.auth.data.datastore.UserData
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.data.mapper.toAuthedUser
import com.example.mustmarket.features.auth.data.mapper.toLoginResult
import com.example.mustmarket.features.auth.domain.model.RefreshToken
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException as RetrofitHttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManger: SessionManager,
    private val userStoreManager: UserStoreManager,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) : AuthRepository {
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
                if (response.message == "Success") {
                    emit(Resource.Success(data = response.toLoginResult()))
                    emit(Resource.Loading(false))
                    sessionManger.saveTokens(response.data.token, response.data.refreshToken)
                    val newUserData = UserData(
                        id = response.data.user.id.toString(),
                        name = response.data.user.name,
                        email = response.data.user.email,
                        lastLoginTimeStamp = System.currentTimeMillis()
                    )

                    if (userStoreManager.isFirstLogin()) {
                        userStoreManager.saveUserData(newUserData)
                    } else {
                        userStoreManager.updateUserData(newUserData)
                    }
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
            }

        }

    override suspend fun logout(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))
        val isValid = sessionManger.isSessionValid()
        if (!isValid) {
            sessionManger.clearTokens()
            userStoreManager.clearUserData()
            categoryDao.clearAllCategory()
            productDao.clearAllProducts()
        }
        emit(Resource.Success(data = isValid))
        emit(Resource.Loading(false))
    }

    override suspend fun requestOtp(email: RequestPasswordReset): Flow<Resource<OtpResponse>> =
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

    override suspend fun resetPassword(otpRequest: OtpRequest): Flow<Resource<PasswordResetResponse>> =
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

    override suspend fun refreshTokenFromServer(refreshToken: RefreshToken): Flow<Resource<AuthedUser>> =
        flow {
            emit(Resource.Loading(true))

            try {
                val response = authApi.refreshToken(refreshToken)
                if (response.message == SUCCESS_RESPONSE) {
                    val user = response.data.toAuthedUser()
                    emit(Resource.Success(data = user))
                    emit(Resource.Loading(false))
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
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }

}