package com.example.mustmarket.features.cart.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun CartScreen(modifier: Modifier = Modifier) {
    Scaffold() { paddingValues ->
        Box(
            modifier = modifier.padding(
                horizontal = paddingValues.calculateLeftPadding(
                    layoutDirection = LayoutDirection.Ltr

                ),
                vertical = paddingValues.calculateTopPadding()
            )
        ) {

        }
    }
}