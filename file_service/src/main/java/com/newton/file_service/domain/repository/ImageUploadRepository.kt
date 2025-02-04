package com.newton.file_service.domain.repository

import com.newton.file_service.domain.model.ImageUploadState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageUploadRepository {
    suspend fun uploadSingleImage(
        image: File,
        onProgress: (Int) -> Unit = {}
    ): Flow<ImageUploadState>
    suspend fun uploadMultipleImages(
        images: List<File>,
        onProgress: (Int) -> Unit
    ): Flow<ImageUploadState>

    fun validateImage(file: File): Boolean
}