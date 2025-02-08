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
<<<<<<< HEAD:app/src/main/java/com/example/mustmarket/navigation/NavGraph.kt
import com.example.mustmarket.features.account.presentation.view.AccountScreen
import com.example.mustmarket.features.auth.presentation.forgotPassword.view.ForgotPasswordRoute
import com.example.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.example.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.example.mustmarket.features.bookmarks.BookmarksScreen
import com.example.mustmarket.features.home.presentation.view.productDetails.ProductDetailsScreen
import com.example.mustmarket.features.home.presentation.view.productList.AllProductsListScreen
import com.example.mustmarket.features.home.presentation.view.productList.HomeScreen
import com.example.mustmarket.features.home.presentation.view.productList.ProductSearchScreen
import com.example.mustmarket.features.home.presentation.viewmodels.AllProductsViewModel
import com.example.mustmarket.features.inbox.chat.view.ChatScreen
import com.example.mustmarket.features.inbox.chat.view.InviteFriendsScreen
import com.example.mustmarket.features.inbox.chatsList.view.ChatListScreen
import com.example.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.example.mustmarket.features.inbox.fetchUsers.view.UserListScreen
import com.example.mustmarket.features.merchant.products.presentation.view.UploadProducts
import com.example.mustmarket.features.merchant.store.view.RegisterStoreScreen
import com.example.mustmarket.features.merchant.store.presentation.StoreProfileScreen
import com.example.mustmarket.features.merchant.store.view.MerchantProductScreen
import com.example.mustmarket.features.merchant.store.view.MerchantStoreScreen
import com.example.mustmarket.features.onboarding.presentation.view.OnboardingScreen
import com.example.mustmarket.features.splash.view.SplashScreen
=======
import com.newton.mustmarket.features.account.presentation.view.AccountScreen
import com.newton.mustmarket.features.auth.presentation.forgotPassword.view.ForgotPasswordRoute
import com.newton.mustmarket.features.auth.presentation.login.view.LoginScreen
import com.newton.mustmarket.features.auth.presentation.signup.view.SignUpScreen
import com.newton.mustmarket.features.bookmarks.BookmarksScreen
import com.newton.mustmarket.features.get_products.presentation.view.productDetails.ProductDetailsScreen
import com.newton.mustmarket.features.get_products.presentation.view.productList.AllProductsListScreen
import com.newton.mustmarket.features.get_products.presentation.view.productList.HomeScreen
import com.newton.mustmarket.features.get_products.presentation.view.productList.ProductSearchScreen
import com.newton.mustmarket.features.get_products.presentation.view.productList.ProductsByCategoryScreen
import com.newton.mustmarket.features.get_products.presentation.viewmodels.AllProductsViewModel
import com.newton.mustmarket.features.inbox.chat.view.ChatScreen
import com.newton.mustmarket.features.inbox.chat.view.NewChatScreen
import com.newton.mustmarket.features.inbox.chatsList.view.ChatListScreen
import com.newton.mustmarket.features.inbox.chatsList.viewModel.ChatListViewModel
import com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_input.MerchantSignupScreen
import com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_onboarding.MerchantOnboardingScreen
import com.newton.mustmarket.features.merchant.get_merchants.presentation.view.MerchantDetailsScreen
import com.newton.mustmarket.features.merchant.upload_products.presentation.view.UploadProducts
import com.newton.mustmarket.features.onboarding.presentation.view.OnboardingScreen
import com.newton.mustmarket.features.splash.view.SplashScreen
>>>>>>> main:app/src/main/java/com/newton/mustmarket/navigation/NavGraph.kt

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
<<<<<<< HEAD:app/src/main/java/com/example/mustmarket/navigation/NavGraph.kt
        startDestination = Screen.Splash.route,
        //Screen.MerchantStore.route
=======
        startDestination = Screen.Splash.route,
>>>>>>> main:app/src/main/java/com/newton/mustmarket/navigation/NavGraph.kt
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
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            ),
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
            //InviteFriendsScreen(navController)
            UserListScreen(
                navController = navController
            )
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
            MerchantOnboardingScreen(
                onRegisterMerchantClick = {
                },
                navController = navController
                )

        }

        composableWithAnimations(route = Screen.MerchantRegistration.route) {
            MerchantSignupScreen(
                onNavigateToMyStore = {}
            )

        }

        composableWithAnimations(route = Screen.ProductByCategory.route, arguments = listOf(
            navArgument("category") { type = NavType.StringType
            }
        )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
                ?: return@composableWithAnimations
            ProductsByCategoryScreen(
                navController = navController,
                categoryName =  category
            )

        }

        composableWithAnimations(route = Screen.GetMerchantById.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) { backStackEntry ->
            val merchantId = backStackEntry.arguments?.getInt("id")
                ?: return@composableWithAnimations
            MerchantDetailsScreen(
                merchantId = merchantId
            )
        }

<<<<<<< HEAD:app/src/main/java/com/example/mustmarket/navigation/NavGraph.kt
        composable("store_profile_screen") {
            StoreProfileScreen(
                navController = navController
            )
        }

        composable(route = Screen.InventoryProducts.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                )
            }) {
            MerchantProductScreen(
                navController = navController
            )
        }

        composable(Screen.InviteFriends.route) {
            // InviteFriendsScreen()
            InviteFriendsScreen(
                navController
            )

        }




=======
>>>>>>> main:app/src/main/java/com/newton/mustmarket/navigation/NavGraph.kt
    }
}

