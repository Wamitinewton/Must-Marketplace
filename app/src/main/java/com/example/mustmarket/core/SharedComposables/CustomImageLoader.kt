package com.example.mustmarket.core.SharedComposables

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.example.mustmarket.BuildConfig
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.shimmer.ShimmerAnimation
import okhttp3.OkHttpClient
import okhttp3.Request
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@Composable
fun CustomImageLoader(
    context: Context,
    s3Url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    fun generateSignedUrl(urlString: String): String {
        val awsCreds =
            AwsBasicCredentials.create(BuildConfig.AWS_ACCESS_KEY, BuildConfig.AWS_SECRET_KEY)
        val credentialsProvider = StaticCredentialsProvider.create(awsCreds)

        val signer = S3Presigner.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(credentialsProvider)
            .build()
        val objectKey: String = urlString.split("/").last()
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(BuildConfig.AWS_BUCKET_NAME)
            .key(objectKey)
            .build()
        val signRequest = GetObjectPresignRequest
            .builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofDays(2))
            .build()
        val signedGetObjectRequest = signer.presignGetObject(signRequest)
        return signedGetObjectRequest.url().toString()
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = generateSignedUrl(s3Url),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
        )

    }

}
