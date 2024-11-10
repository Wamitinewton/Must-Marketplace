package com.example.mustmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.mustmarket.core.SharedComposables.BottomNavBar
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.navigation.SetUpNavGraph
import com.example.mustmarket.ui.theme.ThemeUtils.MustMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val navController = rememberNavController()
            MustMarketTheme {
                Scaffold(
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                        val showBottomBar =
                            currentRoute in Screen.BottomNavItems.items.map { it.route }
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    SetUpNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        onNavigateToHome = {}
                    )

                }

            }
        }
    }
}

