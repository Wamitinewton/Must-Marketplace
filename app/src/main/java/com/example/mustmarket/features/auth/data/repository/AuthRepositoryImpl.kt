package com.example.mustmarket.features.auth.data.repository

<<<<<<< HEAD
import coil.network.HttpException
import com.example.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.database.mappers.toAuthedUser
import com.example.mustmarket.database.mappers.toUserEntity
=======
import com.example.mustmarket.core.util.Constants.SUCCESS_RESPONSE
import com.example.mustmarket.core.util.Resource
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.auth.data.dto.OtpResponse
import com.example.mustmarket.features.auth.data.dto.PasswordResetResponse
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.datastore.SessionManager
<<<<<<< HEAD
=======
import com.example.mustmarket.features.auth.data.datastore.UserData
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.OtpRequest
import com.example.mustmarket.features.auth.domain.model.RequestPasswordReset
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.data.mapper.toAuthedUser
import com.example.mustmarket.features.auth.data.mapper.toLoginResult
import com.example.mustmarket.features.auth.data.tokenHolder.AuthTokenHolder
<<<<<<< HEAD
import com.example.mustmarket.features.auth.domain.model.LoginData
import com.example.mustmarket.features.auth.domain.model.RefreshToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import timber.log.Timber
=======
import com.example.mustmarket.features.auth.domain.model.RefreshToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException as RetrofitHttpException

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManger: SessionManager,
<<<<<<< HEAD
    private val userDao: UserDao
) : AuthRepository {

    init {
        runBlocking {
            AuthTokenHolder.initialize(sessionManger)
            Timber.d("Successfully initialized")
        }
    }


=======
    
    private val userStoreManager: UserStoreManager
) : AuthRepository {
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
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
<<<<<<< HEAD
                if (response.message == SUCCESS_RESPONSE) {
                    emit(Resource.Success(data = response.toLoginResult()))
                    emit(Resource.Loading(false))
                } else {
                    emit(Resource.Error(response.message))
                }
=======
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

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = e.message
                            ?: "Could not reach server, check your internet connection",
                    )
                )
            }

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

<<<<<<< HEAD
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

=======
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
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa

    override suspend fun storeAuthTokens(accessToken: String, refreshToken: String) {
        AuthTokenHolder.accessToken = accessToken
        AuthTokenHolder.refreshToken = refreshToken
<<<<<<< HEAD
        sessionManger.saveTokens(
            accessToken,
            refreshToken
        )

        // Debug: Check token persistence
        val savedAccessToken = sessionManger.fetchAccessToken()
        val savedRefreshToken = sessionManger.fetchRefreshToken()
        Timber.d("Tokens saved successfully: AccessToken=$savedAccessToken, RefreshToken=$savedRefreshToken")
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
=======
        sessionManger.updateTokens(
            accessToken,
            refreshToken
        )
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
    }

}