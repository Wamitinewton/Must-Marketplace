package com.example.mustmarket.features.home.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductImageDto(
    @SerialName("imageId")
    val imageId: Long,

    @SerialName("imageName")
    val imageName: String? = null,

    @SerialName("downloadUrl")
    val downloadUrl: String
)