package com.newton.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordReset(
    val email: String
)
