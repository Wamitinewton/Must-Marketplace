package com.newton.file_service.data.remote.api_service

import com.newton.file_service.data.remote.response.UploadImageListOfImageResponse
import com.newton.file_service.data.remote.response.UploadSingleImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadApi {
    @Multipart
    @POST("upload/single")
    suspend fun uploadSingleImage(
        @Part image: MultipartBody.Part
    ): UploadSingleImageResponse

    @Multipart
    @POST("upload/single")
    suspend fun uploadMultipleImages(
        @Part image: List<MultipartBody.Part>
    ): UploadImageListOfImageResponse
}