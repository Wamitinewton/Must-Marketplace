package com.example.mustmarket.features.uploadProducts.domain.s3Service

import android.net.Uri
import com.example.mustmarket.core.util.Resource
import com.example.mustmarket.features.uploadProducts.data.s3ServiceImpl.s3Data.UploadProgress
import kotlinx.coroutines.flow.Flow

interface S3Service {
    suspend fun uploadImages(images: List<Uri>): Flow<Resource<List<UploadProgress>>>
}