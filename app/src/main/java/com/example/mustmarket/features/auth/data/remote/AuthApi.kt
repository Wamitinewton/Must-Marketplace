package com.example.mustmarket.features.auth.data.remote

import com.example.mustmarket.features.auth.domain.model.FinalUser
import com.example.mustmarket.features.auth.domain.model.SignUpResult
import com.example.mustmarket.features.auth.domain.model.SignUpUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @GET("/api/me")
    suspend fun loginUser(@Header("Authorization") authToken: String): FinalUser
    // remember to return user from user sessions

    @POST("/api/users")
    suspend fun signUpUser(@Body signUp: SignUpUser): SignUpResult
}

