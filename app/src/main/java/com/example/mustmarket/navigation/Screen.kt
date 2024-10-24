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
    data object Welcome : Screen("welcome_screen")
    data object Login : Screen("login_screen")
    data object SignUp : Screen("sign_up_screen")
    data object HomeScreen: Screen("home_screen")
    data object Detail : Screen("detail_screen")
    data object Order : Screen("order_screen")
    data object Start : Screen("get_started_screen")
    data object Explore: Screen("explore_screen")
    data object Bookmarks: Screen("bookmarks_screen")
    data object Profile: Screen("profile_screen")
    data object Favourites: Screen("favourites_screen")

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