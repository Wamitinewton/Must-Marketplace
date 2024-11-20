package com.example.mustmarket.features.account.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)