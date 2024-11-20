package com.example.mustmarket.features.account.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mustmarket.features.account.domain.models.SettingItem

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ){
        Header(
            title = "Settings",
            navController = navController
            )
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