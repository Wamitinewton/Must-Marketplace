package com.example.mustmarket.features.auth.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.datastore.SessionManager
import com.example.mustmarket.features.auth.domain.model.AuthedUser
import com.example.mustmarket.features.auth.domain.model.LoginRequest
import com.example.mustmarket.features.auth.domain.model.LoginResult
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
    private val sessionManger: SessionManager
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

            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Couldn't reach server, check your internet connection",
                    )
                )
            }
            emit(Resource.Loading(false))
        }

}