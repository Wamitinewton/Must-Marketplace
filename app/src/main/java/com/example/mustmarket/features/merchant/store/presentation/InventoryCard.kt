package com.example.mustmarket.features.merchant.store.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.ui.theme.ThemeUtils
import com.newton.mustmarket.ui.theme.ThemeUtils.themed


@Composable
fun InventoryCard(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit,
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable (
                onClick = onClick
            ),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium
    ){
        Column (
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            icon()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = text,
                color = ThemeUtils.AppColors.Text.themed()
            )
        }
    }
}