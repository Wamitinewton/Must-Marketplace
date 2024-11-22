package com.example.mustmarket.features.auth.domain.model

import com.example.mustmarket.features.auth.data.dto.LoginDataDto
import kotlinx.serialization.Serializable


@Serializable
data class LoginResult(
    val message: String,

    val data: LoginDto
)

@Serializable
data class LoginDto(
    val token: String,
    val refreshToken: String,
    val user: AuthedUser
)
