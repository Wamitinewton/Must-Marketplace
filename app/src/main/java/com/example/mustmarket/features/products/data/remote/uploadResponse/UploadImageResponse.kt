package com.example.mustmarket.features.products.data.remote.uploadResponse

data class UploadImageListOfImageResponse (
    val message: String,
    val data: List<String>
)

data class UploadSingleImageResponse (
    val message: String,
    val data: String
)