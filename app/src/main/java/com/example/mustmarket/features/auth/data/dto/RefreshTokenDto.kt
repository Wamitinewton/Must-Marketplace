package com.example.mustmarket.features.auth.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    @SerializedName("refreshToken")
    val refreshToken: String
)
