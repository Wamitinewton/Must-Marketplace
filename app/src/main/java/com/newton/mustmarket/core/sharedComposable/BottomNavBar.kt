package com.newton.mustmarket.core.sharedComposable

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
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
import com.newton.mustmarket.features.merchant.create_store.presentation.viewModel.MerchantViewModel
import com.newton.mustmarket.navigation.Screen

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    merchantViewModel: MerchantViewModel = viewModel()
) {
    val screens = Screen.BottomNavItems.items
    val isMerchant by merchantViewModel.isMerchant.collectAsState()

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
                            Screen.AddProduct -> {
                                if (isMerchant) Icons.Default.Shop else Icons.Default.AddCircleOutline
                            }
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
                            Screen.AddProduct -> {
                                if (isMerchant) "My Shop" else "Register"
                            }
                            Screen.Profile -> "Account"
                            else -> ""
                        },
                        fontSize = 12.sp
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        when (screen) {
                            Screen.AddProduct -> {
                                val route = if (isMerchant) {
                                    Screen.AddProduct.route
                                } else {
                                    Screen.MerchantOnboarding.route
                                }
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            else -> {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
//                        navController.popBackStack()
                    }
                }
            )
        }
    }
}