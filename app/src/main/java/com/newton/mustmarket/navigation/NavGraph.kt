package com.newton.mustmarket.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.newton.mustmarket.features.account.presentation.view.AccountScreen
import com.newton.mustmarket.features.auth.presentation.forgotPassword.view.ForgotPasswordRoute
import com.newton.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.newton.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.newton.mustmarket.features.bookmarks.BookmarksScreen
import com.newton.mustmarket.features.home.presentation.view.productDetails.ProductDetailsScreen
import com.newton.mustmarket.features.home.presentation.view.productList.AllProductsListScreen
import com.newton.mustmarket.features.home.presentation.view.productList.HomeScreen
import com.newton.mustmarket.features.home.presentation.view.productList.ProductSearchScreen
import com.newton.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.newton.mustmarket.features.inbox.chat.view.ChatScreen
import com.newton.mustmarket.features.inbox.chat.view.NewChatScreen
import com.newton.mustmarket.features.inbox.chatsList.view.ChatListScreen
import com.newton.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_onboarding.MerchantOnboardingScreen
import com.newton.mustmarket.features.merchant.products.presentation.view.UploadProducts
import com.newton.mustmarket.features.onboarding.presentation.view.OnboardingScreen
import com.newton.mustmarket.features.splash.view.SplashScreen

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    productViewModel: AllProductsViewModel = hiltViewModel(),
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MerchantOnboarding.route,
    ) {
        composableWithAnimations(route = Screen.Onboarding.route) { OnboardingScreen(navController = navController) }
        composableWithAnimations(
            route = Screen.SignUp.route,
            navTransition = NavigationTransitions.fade()
        ) { SignUpScreen(navController = navController) }
        composableWithAnimations(
            route = Screen.Login.route,
            navTransition = NavigationTransitions.fade()
        ) { LoginScreen(navController = navController) }
        composableWithAnimations(
            route = Screen.Splash.route,
            navTransition = NavigationTransitions.horizontalSlide()
          ) { SplashScreen(navController = navController) }
        composableWithAnimations(
            route = Screen.HomeScreen.route,
            navTransition = NavigationTransitions.fade()
            ) {
            HomeScreen(
                navController = navController,
                allProductsViewModel = productViewModel
            )
        }
        composableWithAnimations(
            route = Screen.Detail.route,
            navTransition = NavigationTransitions.verticalSlide(),
//            arguments = listOf(
//                navArgument("productId") { type = NavType.IntType }
//            ),
          ) {
            ProductDetailsScreen(
                navController = navController,
                onBackPressed = { navController.popBackStack() }
            )
        }

        composableWithAnimations(
            route = Screen.ChatListScreen.route,
           ) {
            val chatListViewModel: ChatListViewModel = hiltViewModel()
            ChatListScreen(
                navController = navController,
                chatListViewModel = chatListViewModel
            )
        }

        composableWithAnimations(
            route = Screen.ChatScreen.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("contactName") {type = NavType.StringType},
                navArgument("currentUser") { type = NavType.StringType}
            ),
          ) { backStackEntry ->
            ChatScreen(
                navController = navController,
                chatId = backStackEntry.arguments?.getString("chatId") ?: "",
                contactName = backStackEntry.arguments?.getString("contactName") ?: "",
                currentUser = backStackEntry.arguments?.getString("currentUser") ?: ""
            )
        }

        composableWithAnimations(Screen.NewChat.route,
          ) {
            NewChatScreen(navController)
        }

        composableWithAnimations(
            route = Screen.Bookmarks.route,
            navTransition = NavigationTransitions.horizontalSlide()
           ) {
            BookmarksScreen(
                navController = navController
            )
        }
        composableWithAnimations(
            route = Screen.Profile.route,
            navTransition = NavigationTransitions.verticalSlide()
          ) {

            AccountScreen(
                navController = navController
            )
        }
        composableWithAnimations(route = Screen.AddProduct.route,) {
            UploadProducts(
                navController = navController
                //userStoreManager = UserStoreManager(context)
            )
        }

        composableWithAnimations(route = Screen.Otp.route) {
            ForgotPasswordRoute(navController = navController, onNavigateToLogin = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route)
            })
        }
        composableWithAnimations(route = Screen.ProductSearch.route) {
            ProductSearchScreen(navController = navController)
        }

        composableWithAnimations(route = Screen.AllProductsList.route) {
            AllProductsListScreen(navController = navController)
        }

        composableWithAnimations(route = Screen.MerchantOnboarding.route) {
            MerchantOnboardingScreen()
        }
    }
}

