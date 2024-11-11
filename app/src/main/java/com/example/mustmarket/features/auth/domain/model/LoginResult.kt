package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
)
