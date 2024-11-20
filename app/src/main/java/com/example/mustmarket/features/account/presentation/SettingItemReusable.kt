package com.example.mustmarket.features.account.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.features.account.domain.models.SettingItem

@Composable
fun SettingsItem(
    setting: SettingItem
){
    Row (
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .clickable {
                setting.onClick()
            }
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ){
            Icon(
                imageVector = setting.icon,
                contentDescription = "leading icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(Modifier.width(30.dp))

            Text(
                text = setting.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black

            )
        }
    }
}