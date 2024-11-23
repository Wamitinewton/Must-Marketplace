package com.example.mustmarket.features.products.data.remote.uploadResponse

import com.example.mustmarket.features.products.domain.models.UploadData
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