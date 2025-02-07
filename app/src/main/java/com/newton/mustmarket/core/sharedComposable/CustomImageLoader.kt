package com.newton.mustmarket.core.sharedComposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage


@Composable
fun CustomImageLoader(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {

    Box(modifier = modifier) {

        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            loading = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = coil.singleton.R.drawable.ic_100tb),
                        contentDescription = "no-image",
                        contentScale = contentScale
                    )
                }
            }
        )
    }

}

