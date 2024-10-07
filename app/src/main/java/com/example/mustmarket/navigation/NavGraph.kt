package com.example.mustmarket.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.example.mustmarket.features.auth.presentation.login.LoginScreen
import com.example.mustmarket.features.auth.presentation.signup.SignUpScreen
import com.example.mustmarket.features.auth.presentation.splash.SplashScreen

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(
    navController: NavHostController
) {
    // mbona unaeka pading kwa routing ;)
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.SignUp.route) { SignUpScreen(navController = navController) }
        composable(route = Screen.Login.route) { LoginScreen(navController = navController) }
        composable(route = Screen.Splash.route) { SplashScreen(navController = navController) }
    }
}