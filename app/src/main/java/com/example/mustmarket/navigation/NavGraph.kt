package com.example.mustmarket.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.example.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.example.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.example.mustmarket.features.auth.presentation.splash.view.SplashScreen
import com.example.mustmarket.features.home.presentation.view.HomeScreen
import com.example.mustmarket.features.product.presentation.details.ProductDetailsScreen

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.SignUp.route) { SignUpScreen(navController = navController) }
        composable(route = Screen.Login.route) { LoginScreen(navController = navController) }
        composable(route = Screen.Splash.route) { SplashScreen(navController = navController) }
        composable(route = Screen.HomeScreen.route){ HomeScreen(navController = navController) }
        composable(route = Screen.Detail.route){ ProductDetailsScreen() }
    }
}