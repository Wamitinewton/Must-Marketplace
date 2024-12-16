package com.example.mustmarket.features.chat.presentation

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ChatBubbleShape(
    private val cornerRadius: Dp = 12.dp,
    private val tipSize: Dp = 10.dp,
    private val isSentByCurrentUser: Boolean = true
): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tipSizePx = with(density) { tipSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        val path = Path().apply {
            if (isSentByCurrentUser) {
                addRoundRect(
                    RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width - tipSizePx,
                        bottom = size.height,
                        topLeftCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        topRightCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        bottomLeftCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        bottomRightCornerRadius = CornerRadius(0f, 0f)
                    )
                )

                moveTo(// Start of triangle
                    x = size.width - tipSizePx,
                    y = size.height - tipSizePx
                )
                lineTo(// Tip point
                    x = size.width,
                    y = size.height
                )
                lineTo(// Base of triangle
                    x = size.width - tipSizePx,
                    y = size.height
                )

                close()

            } else {
                // Received message (tip on the left)
                addRoundRect(
                    RoundRect(
                        left = tipSizePx,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        topLeftCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        topRightCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        bottomLeftCornerRadius = CornerRadius(0f, 0f),
                        bottomRightCornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                    )
                )

                moveTo(// Start of triangle
                    x = tipSizePx,
                    y = size.height - tipSizePx
                )
                lineTo(// Tip point
                    x = 0f,
                    y = size.height
                )
                lineTo(// Base of triangle
                    x = tipSizePx,
                    y = size.height
                )

                close()
            }

        }

        return Outline.Generic(path)
    }
}