package com.newton.mustmarket.features.auth.data.remote.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class OtpResponse(
    val message: String,
)
