package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String
)
