package com.example.mustmarket.features.auth.domain.repository

import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.auth.domain.model.FinalUser
import com.example.mustmarket.features.auth.domain.model.LoginResult
import com.example.mustmarket.features.auth.domain.model.LoginUser
import com.example.mustmarket.features.auth.domain.model.SignUpResult
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(signUp: SignUpUser): Flow<Resource<SignUpResult>>
    suspend fun loginUser(loginCredentials: LoginUser): Flow<Resource<LoginResult>>
    suspend fun login(authToken: String): FinalUser
}