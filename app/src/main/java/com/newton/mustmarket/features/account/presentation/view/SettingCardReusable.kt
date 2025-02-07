package com.newton.mustmarket.features.account.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.features.account.domain.models.SettingItem
import com.newton.mustmarket.ui.theme.Shapes
import com.newton.mustmarket.ui.theme.gray01

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
                    MaterialTheme.colors.surface,
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