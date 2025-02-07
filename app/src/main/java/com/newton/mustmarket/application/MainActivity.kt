package com.newton.mustmarket.application

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
import com.newton.mustmarket.core.sharedComposable.BottomNavBar
import com.newton.mustmarket.features.auth.data.authWorkManager.scheduleTokenRefreshWork
import com.newton.mustmarket.navigation.Screen
import com.newton.mustmarket.navigation.SetUpNavGraph
import com.newton.mustmarket.ui.theme.ThemeUtils.MustMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleTokenRefreshWork(applicationContext)
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
                    )

                }

            }

        }
    }

}

