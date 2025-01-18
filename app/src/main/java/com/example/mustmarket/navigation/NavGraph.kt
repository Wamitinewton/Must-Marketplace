package com.example.mustmarket.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.mustmarket.features.account.presentation.view.AccountScreen
import com.example.mustmarket.features.auth.data.datastore.UserStoreManager
import com.example.mustmarket.features.auth.presentation.forgotPassword.view.ForgotPasswordRoute
import com.example.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.example.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.example.mustmarket.features.bookmarks.BookmarksScreen
import com.example.mustmarket.features.inbox.chat.view.ChatScreen
import com.example.mustmarket.features.inbox.chatsList.view.ChatListScreen
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.features.home.presentation.view.productDetails.ProductDetailsScreen
import com.example.mustmarket.features.home.presentation.view.productList.AllProductsListScreen
import com.example.mustmarket.features.home.presentation.view.productList.HomeScreen
import com.example.mustmarket.features.home.presentation.view.productList.ProductSearchScreen
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.inbox.chat.view.NewChatScreen
import com.example.mustmarket.features.inbox.chatsList.model.Chat
import com.example.mustmarket.features.merchant.products.presentation.view.UploadProducts
import com.example.mustmarket.features.onboarding.presentation.view.OnboardingScreen
import com.example.mustmarket.features.splash.view.SplashScreen

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    productViewModel: AllProductsViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(route = Screen.Onboarding.route) { OnboardingScreen(navController = navController) }
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
            }) {
            HomeScreen(
                navController = navController,
                allProductsViewModel = productViewModel
            )
        }
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
                navController = navController,
                onBackPressed = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ChatListScreen.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {
            val chatListViewModel: ChatListViewModel = viewModel()
            ChatListScreen(
                navController = navController,
                chatListViewModel = chatListViewModel
            )
        }

        composable(
            route = Screen.ChatScreen.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType }),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            val recipientName = backStackEntry.arguments?.getString("recipientName") ?: return@composable
            ChatScreen(
                navController = navController,
                chatId = chatId ?: "",
                currentUser = "user!",
                contactName = recipientName
            )
        }

        composable(Screen.NewChat.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {
            NewChatScreen(navController)
        }

        composable(route = Screen.Bookmarks.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {
            BookmarksScreen(
                navController = navController
            )
        }
        composable(route = Screen.Profile.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {

            AccountScreen(
                navController = navController
            )
        }
        composable(route = Screen.Favourites.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {
            UploadProducts(
                userStoreManager = UserStoreManager(context)
            )
        }

        composable(route = Screen.Otp.route) {
            ForgotPasswordRoute(navController = navController, onNavigateToLogin = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route)
            })
        }
        composable(route = Screen.ProductSearch.route) {
            ProductSearchScreen(navController = navController)
        }

        composable(route = Screen.AllProductsList.route) {
            AllProductsListScreen(navController = navController)
        }
    }
}

