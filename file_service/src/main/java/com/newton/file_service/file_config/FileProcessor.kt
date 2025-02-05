package com.newton.mustmarket.core.file_config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileProcessor(
    private val config: ImageProcessingConfig = ImageProcessingConfig(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
     suspend fun processImage(file: File): File = withContext(dispatcher) {
        return@withContext processAndOptimizeFile(file)
    }



    private fun processAndOptimizeFile(file: File): File {
        var bitmap: Bitmap? = null
        var scaledBitmap: Bitmap? = null
        var outputFile: File? = null

        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.path, options)

            options.apply {
                inSampleSize = calculateInSampleSize(options)
                inJustDecodeBounds = false
            }
            bitmap = BitmapFactory.decodeFile(file.path, options)
                ?: throw IOException("Failed to decode image: ${file.name}")

            scaledBitmap = scaleBitmap(bitmap)

            outputFile = createTempFile()
            FileOutputStream(outputFile).use { out ->
                scaledBitmap.compress(config.format, config.compressionQuality.toInt(), out)
            }
            return outputFile
        } catch (e: Exception) {
            outputFile?.delete()
            throw e
        } finally {
            if (scaledBitmap != bitmap) scaledBitmap?.recycle()
            bitmap?.recycle()
        }
    }


    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (width, height) = options.run { outWidth to outHeight }
        var inSampleSize = 1

        if (height > config.maxDimension || width > config.maxDimension) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfWidth / inSampleSize) > config.maxDimension ||
                    (halfHeight / inSampleSize) >= config.maxDimension) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val (width, height) = bitmap.run { width to height }
        if (width <= config.maxDimension && height <= config.maxDimension) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val (newWidth, newHeight) = if (width > height) {
            config.maxDimension to (config.maxDimension / ratio).toInt()
        } else {
            (config.maxDimension * ratio).toInt() to config.maxDimension
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


    private fun createTempFile(): File {
        return File.createTempFile("optimized_", ".jpg").apply {
            deleteOnExit()
        }
    }
}