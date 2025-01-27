package com.example.mustmarket.features.auth.data.remote.auth_response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LogInResultDto(
   @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: LoginDataDto
)

