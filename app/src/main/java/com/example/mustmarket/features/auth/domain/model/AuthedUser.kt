package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AuthedUser(
    val id: Int,
    val userName: String,
    val email: String
)
