package com.example.mustmarket.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class LogInResultDto(
    @SerializedName("accesstoken")
    val accessToken: String?,
    @SerializedName("refreshtoken")
    val refreshToken: String?
)