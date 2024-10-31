package com.example.mustmarket.core.SharedComposables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
 fun ErrorState() {
    Box(
        modifier = Modifier
            .height(90.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 1.dp, color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center,


        ) {
        Text(
            text = "Server error. Try again later",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )
    }
}