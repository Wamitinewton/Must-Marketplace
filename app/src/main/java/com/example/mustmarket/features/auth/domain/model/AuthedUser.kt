package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AuthedUser(
    val id: String,
    val name: String,
    val email: String
)
