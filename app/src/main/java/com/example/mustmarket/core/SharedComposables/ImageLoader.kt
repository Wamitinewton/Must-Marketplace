package com.example.mustmarket.core.SharedComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mustmarket.R

@Composable
fun ImageLoaderUtil(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(16.dp),
    showProgressIndicator: Boolean = true,
    progressIndicatorSize: Int = 48,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    surfaceColor: Color = MaterialTheme.colors.surface,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(shape)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = {
                isLoading = false
                isError = true
            }
        )

        if (isLoading && showProgressIndicator) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = progressIndicatorColor,
                    modifier = Modifier.size(progressIndicatorSize.dp)
                )
            }
        }

        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_image),
                    contentDescription = "no-image",
                    contentScale = contentScale
                )
            }
        }
    }
}