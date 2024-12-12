package com.example.mustmarket.features.account.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.OutlinedFlag
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mustmarket.features.account.domain.models.SettingItem
import com.example.mustmarket.features.auth.presentation.login.enums.AuthState
import com.example.mustmarket.features.auth.presentation.login.event.LoginEvent
import com.example.mustmarket.features.auth.presentation.login.viewmodels.LoginViewModel
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.ui.theme.ThemeUtils
import com.example.mustmarket.ui.theme.ThemeUtils.themed

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                loginViewModel.onEvent(LoginEvent.Logout)
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

    LaunchedEffect(Unit) {
        loginViewModel.navigateToLogin.collect {
            showLogoutDialog = false
            navController.popBackStack()
            navController.navigate(Screen.Login.route)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeUtils.AppColors.Surface.themed())
            .padding(16.dp)
    ) {
        Header(
            title = "Settings"
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
                    label = "Log out",
                    icon = Icons.AutoMirrored.Filled.Logout,
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            )
        )
    }
}