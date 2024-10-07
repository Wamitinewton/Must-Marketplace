package com.example.mustmarket.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mustmarket.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int? = R.string.account,
    @DrawableRes val drawableId: Int? = R.drawable.account
) {
    object Splash : Screen("splash_screen")
    object Welcome : Screen("welcome_screen")
    object Login : Screen("login_screen")
    object SignUp : Screen("sign_up_screen")
    object Detail : Screen("detail_screen")
    object Order : Screen("order_screen")
    object Start : Screen("get_started_screen")
}