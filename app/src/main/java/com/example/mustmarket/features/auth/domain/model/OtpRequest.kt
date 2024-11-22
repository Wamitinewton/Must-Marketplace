package com.example.mustmarket.features.auth.domain.model

data class OtpRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)
