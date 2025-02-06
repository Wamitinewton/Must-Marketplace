package com.newton.file_service.file_config


import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

object FileConverter {
    /**
     * Converts a given URI to a File.
     * Throws an IOException if the file cannot be created.
     */
    @Throws(IOException::class)
    fun uriToFile(context: Context, uri: Uri): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return file
        } ?: throw IOException("Failed to open input stream from URI: $uri")
    }
}
