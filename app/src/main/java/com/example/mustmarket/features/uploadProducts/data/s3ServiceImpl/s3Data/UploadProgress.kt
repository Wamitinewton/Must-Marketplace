package com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data

data class UploadProgress(
    val fileName: String,
    val progress: Float,
    val url: String? = null,
    val error: UploadError? = null,
    val bytesTransferred: Long = 0,
    val totalBytes: Long = 0
)
