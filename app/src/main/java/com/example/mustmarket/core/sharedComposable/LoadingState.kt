package com.example.mustmarket.core.sharedComposable

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    type: LoadingAnimationType = LoadingAnimationType.BOUNCING_DOTS
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            LoadingAnimationType.BOUNCING_DOTS -> BouncingDotsLoadingAnimation()
            LoadingAnimationType.PULSING_DOTS -> PulsingDotsLoadingAnimation()
        }
    }
}

enum class LoadingAnimationType {
    BOUNCING_DOTS,
    PULSING_DOTS
}

private val dotColors = listOf(
    Color(0xFF6200EE),
    Color(0xFF03DAC5),
    Color(0xFFFF6B6B),
    Color(0xFF4CAF50),
    Color(0xFFFFA726),
    Color(0xFF2196F3)
)

@Composable
fun BouncingDotsLoadingAnimation(
    dotSize: Float = 12f,
    delayUnit: Int = 500,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun Dot(
        offset: Float
    ) {
        val scale by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = delayUnit,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(delayUnit / 3 * offset.toInt())
            ), label = ""
        )
        val colorIndex by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = dotColors.size.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = delayUnit * 3,
                    easing = LinearEasing
                ),
                initialStartOffset = StartOffset(delayUnit / 3 * offset.toInt())
            ), label = ""
        )

        val currentColorIndex = colorIndex.toInt() % dotColors.size
        val nextColorIndex = (currentColorIndex + 1) % dotColors.size
        val fraction = colorIndex - colorIndex.toInt()

        val currentColor = lerp(
            start = dotColors[currentColorIndex],
            stop = dotColors[nextColorIndex],
            fraction = fraction
        )

        Box(
            modifier = Modifier
                .size(dotSize.dp)
                .scale(scale)
                .background(
                    color = currentColor,
                    shape = CircleShape
                )
        )
    }

    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Dot(0f)
        Dot(1f)
        Dot(2f)
    }
}

@Composable
fun PulsingDotsLoadingAnimation(
    dotSize: Float = 12f,
    delayUnit: Int = 500,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun Dot(
        offset: Float
    ) {
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = delayUnit,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(delayUnit / 3 * offset.toInt())
            ), label = ""
        )

        val colorIndex by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = dotColors.size.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = delayUnit * 3,
                    easing = LinearEasing
                ),
                initialStartOffset = StartOffset(delayUnit / 3 * offset.toInt())
            ), label = ""
        )

        val currentColorIndex = colorIndex.toInt() % dotColors.size
        val nextColorIndex = (currentColorIndex + 1) % dotColors.size
        val fraction = colorIndex - colorIndex.toInt()
        val currentColor = lerp(
            start = dotColors[currentColorIndex],
            stop = dotColors[nextColorIndex],
            fraction = fraction
        )

        Box(
            modifier = Modifier
                .size(dotSize.dp)
                .background(
                    color = currentColor.copy(alpha = alpha),
                    shape = CircleShape
                )
        )
    }

    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Dot(0f)
        Dot(1f)
        Dot(2f)
    }
}


private fun lerp(start: Color, stop: Color, fraction: Float): Color {
    return Color(
        red = lerp(start.red, stop.red, fraction),
        green = lerp(start.green, stop.green, fraction),
        blue = lerp(start.blue, stop.blue, fraction),
        alpha = lerp(start.alpha, stop.alpha, fraction)
    )
}


private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}