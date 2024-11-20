package com.example.mustmarket.features.auth.presentation.signup.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mustmarket.R
import com.example.mustmarket.core.SharedComposables.LoopReverseLottieLoader
import kotlinx.coroutines.launch

@Composable
fun SignUpSuccess(
    onDismiss: () -> Unit,
) {
    var alpha by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(0.8f) }

    LaunchedEffect(Unit) {
        launch {
            animate(
                0f,
                1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) { value, _ ->
                alpha = value
            }
        }

        launch {
            animate(
                0.8f,
                1f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) { value, _ ->
                scale = value
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoopReverseLottieLoader(
                    modifier = Modifier.size(200.dp),
                    lottieFile = R.raw.success_done
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Welcome Aboard!",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Your account has been successfully created.",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(32.dp))

            }
        }
    }

}