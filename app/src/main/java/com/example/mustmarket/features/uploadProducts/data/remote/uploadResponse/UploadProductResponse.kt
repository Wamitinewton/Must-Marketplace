package com.example.mustmarket.features.uploadProducts.data.remote.uploadResponse

import kotlinx.serialization.Serializable

@Serializable
data class UploadProductResponse(
    val data: UploadData,
    val message: String
)


@Serializable
data class Category(
    val id: Int,
    val image: String,
    val name: String
)