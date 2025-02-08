package com.newton.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)
