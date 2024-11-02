package com.example.mustmarket.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.mustmarket.features.account.presentation.AccountScreen
import com.example.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.example.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.example.mustmarket.features.auth.presentation.splash.view.SplashScreen
import com.example.mustmarket.features.bookmarks.BookmarksScreen
import com.example.mustmarket.features.explore.ExploreScreen
import com.example.mustmarket.features.favourite.FavouritesScreen
import com.example.mustmarket.features.home.presentation.view.productDetails.ProductDetailsScreen
import com.example.mustmarket.features.home.presentation.view.productList.HomeScreen

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        ) {
        composable(route = Screen.SignUp.route, enterTransition = {
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
            )
        }) { SignUpScreen(navController = navController) }
        composable(route = Screen.Login.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { LoginScreen(navController = navController) }
        composable(route = Screen.Splash.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { SplashScreen(navController = navController) }
        composable(route = Screen.HomeScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { HomeScreen(navController = navController) }
        composable(route = Screen.Detail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            ),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                )
            }) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            ProductDetailsScreen(
                productId = productId,
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Explore.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { ExploreScreen() }
        composable(route = Screen.Bookmarks.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { BookmarksScreen(
                onProductClick = {}
            ) }
        composable(route = Screen.Profile.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { AccountScreen() }
        composable(route = Screen.Favourites.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { FavouritesScreen() }
    }
}

