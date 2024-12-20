package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResult(
    val message: String,
<<<<<<< HEAD
=======

>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
    val data: LoginData
)

@Serializable
data class LoginData(
    val token: String,
    val refreshToken: String,
    val user: AuthedUser
)
