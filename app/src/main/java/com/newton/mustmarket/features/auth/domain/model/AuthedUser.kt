package com.newton.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class AuthedUser(
    val id: Int,
    val name: String,
    val email: String,
    val number: String? = null
)
