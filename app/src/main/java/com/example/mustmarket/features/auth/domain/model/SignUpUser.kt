package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class SignUpUser(
    val name: String,
    val email: String,
    val password: String,
)


