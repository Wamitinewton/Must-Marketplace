package com.newton.mustmarket.core.sharedComposable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedText(
    text: String,
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal,
    glowColor: Color = MaterialTheme.colors.primary
) {
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Text(
        text = text,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        modifier = Modifier.drawBehind {
            drawCircle(
                color = glowColor,
                radius = this.size.maxDimension,
                alpha = glowAlpha,
                blendMode = BlendMode.Screen
            )
        }
    )
}