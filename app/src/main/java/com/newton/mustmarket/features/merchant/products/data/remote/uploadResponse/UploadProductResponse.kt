package com.newton.mustmarket.features.merchant.products.data.remote.uploadResponse

import com.newton.mustmarket.features.merchant.products.domain.models.UploadData
import kotlinx.serialization.Serializable

@Serializable
data class UploadProductResponse(
    val message: String,
    val data: UploadData,
)
