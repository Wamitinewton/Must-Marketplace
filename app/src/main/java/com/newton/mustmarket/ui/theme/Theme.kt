package com.newton.mustmarket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object ThemeUtils {
    data class ThemeColorPair(
        val light: Color,
        val dark: Color
    )


    object AppColors {
        val Background = ThemeColorPair(
            light = Color.White,
            dark = Color(0xFF121212)
        )

        val Surface = ThemeColorPair(
            light = Color.White,
            dark = Color(0xFF1E1E1E)
        )

        val Text = ThemeColorPair(
            light = Color(0xFF181725),
            dark = Color.White
        )
        val ChatBubble = ThemeColorPair(
            dark = Color(0xFF1E1E1E),
            light = Color(231, 246, 237)
        )

        val InvertText = ThemeColorPair(
            dark = Color(0xFF181725),
            light = Color.White
        )

        val SecondaryText = ThemeColorPair(
            light = Color(0xFF7C7C7C),
            dark = Color(0xFFB0B0B0)
        )

        val Divider = ThemeColorPair(
            light = Color(0xFFE2E2E2),
            dark = Color(0xFF2A2A2A)
        )

        val Primary = Color(0xff53B175)
        val Purple700 = Color(0xFF3700B3)
        val Teal200 = Color(0xFF03DAC5)
    }

    private val DarkColorPalette = darkColors(
        primary = AppColors.Primary,
        primaryVariant = AppColors.Purple700,
        secondary = AppColors.Teal200,
        background = AppColors.Background.dark,
        surface = AppColors.Surface.dark,
        onBackground = AppColors.Text.dark,
        onSurface = AppColors.Text.dark
    )

    private val LightColorPalette = lightColors(
        primary = AppColors.Primary,
        primaryVariant = AppColors.Purple700,
        secondary = AppColors.Teal200,
        background = AppColors.Background.light,
        surface = AppColors.Surface.light,
        onBackground = AppColors.Text.light,
        onSurface = AppColors.Text.light
    )


    @Composable
    @ReadOnlyComposable
    fun ThemeColorPair.themed(): Color {
        return if (isSystemInDarkTheme()) dark else light
    }


    @Composable
    fun MustMarketTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}