package com.example.mustmarket.core.sharedComposable

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mustmarket.features.merchant.storeRegistration.viewModel.MerchantViewModel
import com.example.mustmarket.navigation.Screen

@Composable


fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    merchantViewModel: MerchantViewModel = viewModel()
) {
    val screens = Screen.BottomNavItems.items
    val isMerchant by merchantViewModel.isMerchant.collectAsState()
    val merchantId by merchantViewModel.merchantId.collectAsState()

    BottomNavigation(
        modifier = modifier,
        elevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            BottomNavigationItem(
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.HomeScreen -> Icons.Default.Home
                            Screen.ChatListScreen -> Icons.AutoMirrored.Filled.Message

                            Screen.Bookmarks -> Icons.Default.Bookmark

                            Screen.AddProduct -> if (isMerchant) Icons.Default.Storefront else Icons.Default.AddCircleOutline
                            Screen.Profile -> Icons.Default.Settings
                            else -> Icons.Default.Home
                        },
                        contentDescription = screen.route
                    )
                },
                label = {
                    Text(
                        text = when (screen) {
                            Screen.HomeScreen -> "Home"
                            Screen.ChatListScreen -> "Messages"
                            Screen.Bookmarks -> "Bookmarks"
                            Screen.AddProduct -> if (isMerchant) "Store" else "Register"
                            Screen.Profile -> "Account"
                            else -> ""
                        },
                        fontSize = 12.sp
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (screen == Screen.AddProduct) {
                        if (isMerchant) {
                            navController.navigate("merchant_store/{merchantId}")
                        } else {
                            navController.navigate("register_store")
                        }
                    } else if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            navController.popBackStack()
                            launchSingleTop = true
                            restoreState = true
                        }
                        }
                }
            )
        }
    }
}