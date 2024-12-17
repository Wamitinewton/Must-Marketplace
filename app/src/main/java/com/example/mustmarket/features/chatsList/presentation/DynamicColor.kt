package com.example.mustmarket.features.chatsList.presentation

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun getRandomBackgroundColor(seed: String): Color {
    val random = Random(seed.hashCode())
    return Color(
        red = random.nextInt(100, 256),
        green = random.nextInt(100, 256),
        blue = random.nextInt(100, 256)
    )
}
