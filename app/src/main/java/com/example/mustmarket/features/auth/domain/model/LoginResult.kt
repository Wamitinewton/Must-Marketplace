package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResult(
    val message: String,

    val data: LoginData
)

@Serializable
data class LoginData(
    val token: String,
    val refreshToken: String,
    val user: AuthedUser
)
