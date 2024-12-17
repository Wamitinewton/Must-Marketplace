package com.example.mustmarket.features.auth.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class LoginDataDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("user")
    val user: UserDto
)
