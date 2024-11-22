package com.example.mustmarket.core.SharedComposables.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerItem(modifier: Modifier, brush: Brush) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(vertical = 8.dp)
            .background(brush = brush),
    )

}