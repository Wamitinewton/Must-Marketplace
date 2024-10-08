package com.example.mustmarket.features.auth.data.repository

import coil.network.HttpException
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.domain.model.FinalUser
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun signUp(signUp: SignUpUser) = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.signUpUser(signUp)
            emit(Resource.Success(data = response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message.toString(),
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message.toString()
                )
            )
        }
    }

    override suspend fun loginUser(loginCredentials: LoginUser): Flow<Resource<LoginResult>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = authApi.loginUser(loginCredentials)
                emit(Resource.Success(data = response))
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        message = e.message.toString()
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "",
                    )
                )
            }
        }

    override suspend fun login(authToken: String): FinalUser {
        TODO("Not yet implemented")
    }
}