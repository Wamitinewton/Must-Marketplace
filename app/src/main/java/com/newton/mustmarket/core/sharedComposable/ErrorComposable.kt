package com.newton.mustmarket.core.sharedComposable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.R

@Composable
fun ErrorComposable(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.error,
    showAnimation: Boolean = true
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible && showAnimation,
            enter = fadeIn(tween(500)) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(700)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Error Icon",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.error)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .alpha(0.87f)
                )
                Button(
                    onClick = onRetry,  // Direct call to onRetry
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSecondary
                    )
                ) {
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    }
}