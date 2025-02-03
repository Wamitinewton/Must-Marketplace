package com.example.mustmarket.core.file_config

import android.graphics.Bitmap
import androidx.annotation.FloatRange

data class ImageProcessingConfig(
    val maxDimension: Int = 2048,
    val maxFileSize: Long = 10 * 1024 * 1024, // 10MB
    @FloatRange(from = 0.0, to = 1.0)
    val compressionQuality: Float = 0.8f,
    val format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
)
