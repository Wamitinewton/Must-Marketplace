package com.newton.mustmarket.features.inbox.chat.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDotsIcon(
    color: Color = Color.LightGray,
    modifier: Modifier
) {
    Canvas(modifier = modifier) {
        val dotSize = 4f
        val spacing = 6.dp.toPx()
        val centerY = size.height / 2

        // Draw 3 dots horizontally
        drawCircle(
            color,
            radius = dotSize,
            center = Offset(0f, centerY)
        )
        drawCircle(
            color,
            radius = dotSize,
            center = Offset(spacing, centerY)
        )
        drawCircle(
            color,
            radius = dotSize,
            center = Offset(spacing * 2, centerY)
        )
    }
}
