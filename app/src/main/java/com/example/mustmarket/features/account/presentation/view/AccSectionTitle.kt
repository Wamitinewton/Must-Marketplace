package com.example.mustmarket.features.account.presentation.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.h3.copy(
            color = ThemeUtils.AppColors.Text.themed()
        ),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}