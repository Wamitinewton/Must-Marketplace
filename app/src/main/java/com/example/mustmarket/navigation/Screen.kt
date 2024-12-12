package com.example.mustmarket.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mustmarket.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int? = R.string.account,
    @DrawableRes val drawableId: Int? = R.drawable.account
) {
    data object Splash : Screen("splash_screen")
    data object Login : Screen("login_screen")
    data object SignUp : Screen("sign_up_screen")
    data object HomeScreen: Screen("home_screen")
    data object Detail : Screen("detail_screen/{productId}"){
        fun createRoute(productId: Int) = "detail_screen/$productId"
    }

    data object Explore: Screen("explore_screen")
    data object Bookmarks: Screen("bookmarks_screen")
    data object Profile: Screen("profile_screen")
    data object Favourites: Screen("favourites_screen")
    data object Onboarding: Screen("onboarding_screen")
    data object Otp: Screen("otp_screen")
    data object ProductSearch: Screen("product_search_screen")
    data object AllProductsList: Screen("all_products_list_screen")

    object BottomNavItems {
        val items = listOf(
            HomeScreen,
            Explore,
            Bookmarks,
            Favourites,
            Profile,
        )
    }
}