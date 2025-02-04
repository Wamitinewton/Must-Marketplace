package com.newton.file_service.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadImageListOfImageResponse (
    val message: String,
    val data: List<String>
)

@Serializable
data class UploadSingleImageResponse (
    val message: String,
    val data: String
)