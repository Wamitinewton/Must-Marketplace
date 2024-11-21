package com.example.mustmarket.features.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mustmarket.features.account.domain.models.SettingItem
import com.example.mustmarket.ui.theme.Shapes
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed
import com.example.mustmarket.ui.theme.gray01

@Composable
fun SettingsCard(
    settings: List<SettingItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding( bottom = 8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clip(
                    shape = Shapes.medium
                )
                .background(
                    gray01,
                    Shapes.medium
                )
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            settings.forEach { item ->
                SettingsItem(item)
            }
        }
    }
}