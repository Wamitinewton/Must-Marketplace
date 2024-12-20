package com.example.mustmarket.application

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
<<<<<<< HEAD
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
import com.example.mustmarket.core.sharedComposable.BottomNavBar
=======
import com.example.mustmarket.core.sharedComposable.BottomNavBar
import com.example.mustmarket.core.coroutineLogger.CoroutineDebugger
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
import com.example.mustmarket.navigation.Screen
import com.example.mustmarket.navigation.SetUpNavGraph
import com.example.mustmarket.ui.theme.ThemeUtils.MustMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

<<<<<<< HEAD

=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineDebugger.enableDebugging()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {

            val navController = rememberNavController()
<<<<<<< HEAD

=======
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
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
<<<<<<< HEAD

        }
    }

=======
        }
    }
>>>>>>> f3e2d5b65c670c1fee62838628eedb0d5e05fdfa
}

