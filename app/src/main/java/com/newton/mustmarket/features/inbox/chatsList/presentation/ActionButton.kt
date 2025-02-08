package com.example.mustmarket.features.inbox.chatsList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String? = null,
    onClick: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        label?.let {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.primarySurface,
                        shape = CircleShape
                    )
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = it,
                    color = ThemeUtils.AppColors.Text.themed()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Floating Action Button
        FloatingActionButton(
            modifier = Modifier.size(50.dp),
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primarySurface
        ) {
            Icon(
                icon,
                contentDescription = label ?: "Action Button"
            )
        }

    }
}