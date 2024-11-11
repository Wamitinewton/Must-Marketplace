package com.example.mustmarket.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String?,
)