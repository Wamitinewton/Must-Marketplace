package com.example.mustmarket.features.auth.data.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UserDto
)
