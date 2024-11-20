package com.example.mustmarket.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.OutlinedFlag
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mustmarket.ui.theme.Shapes
import com.example.mustmarket.ui.theme.gray01

@Composable
fun SettingsScreen(
    //navController: NavController
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {

                    }
                    .size(40.dp)
            )

            Spacer(Modifier.width(56.dp))

            Text(
                text = "Settings",
                fontSize = 30.sp,
                fontWeight = FontWeight.W700,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        SectionTitle("Account")
        SettingsCard(
            settings = listOf(
                SettingItem(
                    label = "Edit profile",
                    icon = Icons.Default.PersonOutline,
                    onClick = {}
                ),
                SettingItem(
                    label = "Security",
                    icon = Icons.Default.PrivacyTip,
                    onClick = {}
                ),
                SettingItem(
                    label = "Notifications",
                    icon = Icons.Default.NotificationsNone,
                    onClick = {}
                ),
                SettingItem(
                    label = "Privacy",
                    icon = Icons.Default.Lock,
                    onClick = {}
                )
            )
        )
        SectionTitle("Support & About")
        SettingsCard(
            settings = listOf(
                SettingItem(
                    label = "My Subscriptions",
                    icon = Icons.Default.CreditCard,
                    onClick = {}
                ),
                SettingItem(
                    label = "Help & Support",
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    onClick = {}
                ),
                SettingItem(
                    label = "Terms & Policies",
                    icon = Icons.Default.ArrowCircleDown,
                    onClick = {}
                )
            )
        )
        SectionTitle("Cache & cellular")
        SettingsCard(
            settings = listOf(
                SettingItem(
                    label = "FreeUp Space",
                    icon = Icons.Default.DeleteForever,
                    onClick = {}
                ),
                SettingItem(
                    label = "Data Saver",
                    icon = Icons.Default.DataExploration,
                    onClick = {}
                )
            )
        )
        SectionTitle("Actions")
        SettingsCard(
            settings = listOf(
                SettingItem(
                    label = "Report a problem",
                    icon = Icons.Default.OutlinedFlag,
                    onClick = {}
                ),
                SettingItem(
                    label = "Add Account",
                    icon = Icons.Default.PeopleOutline,
                    onClick = {}
                ),
                SettingItem(
                    label = "Log out",
                    icon = Icons.AutoMirrored.Filled.Logout,
                    onClick = {}
                )
            )
        )
    }
}

@Composable
fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

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

data class SettingItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)