package com.example.mustmarket.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)
