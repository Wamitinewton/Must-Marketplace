package com.newton.mustmarket.core.adaptableLayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

object ResponsiveSizeUtil {

    private const val BASE_SCREEN_WIDTH = 360f
    private const val BASE_SCREEN_HEIGHT = 640f

    /**
     * Calculates a responsive text size based on the screen's dimensions.
     *
     * @param baseSize The default text size for standard devices.
     * @param scaleFactor The multiplier used to scale the text size (default is 0.05).
     * @return A scaled TextUnit size, constrained within minSize and maxSize.
     */
    @Composable
    fun responsiveTextSize(
        baseSize: TextUnit = 16.sp,
        scaleFactor: Float = 0.03f,
    ): TextUnit {
        val configuration = LocalConfiguration.current
        val minDimension = min(configuration.screenWidthDp, configuration.screenHeightDp).toFloat()
        val normalizedFactor = minDimension / BASE_SCREEN_WIDTH
        val scaledSize = baseSize.value + (baseSize.value * (normalizedFactor - 1) * scaleFactor)
        return scaledSize.coerceIn(baseSize.value, baseSize.value * 1.5f).sp
    }

    /**
     * Calculates a responsive dimension (width or height) based on the screen size.
     *
     * @param baseDimension The base dimension for standard devices.
     * @param type The type of dimension to calculate (WIDTH or HEIGHT).
     * @param scaleFactor The multiplier used to scale the dimension.
     * @return A scaled Dp dimension.
     */
    @Composable
    fun responsiveDimension(
        baseDimension: Dp,
        type: DimensionType = DimensionType.WIDTH,
        scaleFactor: Float = 0.05f
    ): Dp {
        val configuration = LocalConfiguration.current
        val dimension = when (type) {
            DimensionType.WIDTH -> configuration.screenWidthDp.toFloat()
            DimensionType.HEIGHT -> configuration.screenHeightDp.toFloat()
        }
        val normalizedFactor = dimension / if (type == DimensionType.WIDTH) BASE_SCREEN_WIDTH else BASE_SCREEN_HEIGHT
        val scaledValue = baseDimension.value + (baseDimension.value * (normalizedFactor - 1) * scaleFactor)
        return scaledValue.coerceIn(baseDimension.value, baseDimension.value * 1.5f).dp
    }
    /**
     * Calculates responsive padding based on the screen's dimensions.
     *
     * @param basePadding The default padding for standard devices.
     * @param scaleFactor The multiplier used to scale the padding.
     * @return A scaled Dp padding value.
     */
    @Composable
    fun responsivePadding(
        basePadding: Dp = 16.dp,
        scaleFactor: Float = 0.02f
    ): Dp {
        val configuration = LocalConfiguration.current
        val minDimension = min(configuration.screenWidthDp, configuration.screenHeightDp).toFloat()
        val normalizedFactor = minDimension / BASE_SCREEN_WIDTH
        val scaledPadding = basePadding.value + (basePadding.value * (normalizedFactor - 1) * scaleFactor)
        return scaledPadding.coerceIn(basePadding.value, basePadding.value * 1.5f).dp
    }

    /**
     * Calculates a percentage-based width.
     *
     * @param percentage The percentage of the screen width (0.0 to 1.0).
     * @return A Dp value representing the percentage of the screen width.
     */
    @Composable
    fun screenWidthPercentage(percentage: Float): Dp {
        val configuration = LocalConfiguration.current
        val width = configuration.screenWidthDp
        return (width * percentage).dp
    }

    /**
     * Calculates a percentage-based height.
     *
     * @param percentage The percentage of the screen height (0.0 to 1.0).
     * @return A Dp value representing the percentage of the screen height.
     */
    @Composable
    fun screenHeightPercentage(percentage: Float): Dp {
        val configuration = LocalConfiguration.current
        val height = configuration.screenHeightDp
        return (height * percentage).dp
    }
}