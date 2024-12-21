package com.example.mustmarket.navigation

sealed class Screen(
    val route: String,

) {
    data object Splash : Screen("splash_screen")
    data object Login : Screen("login_screen")
    data object SignUp : Screen("sign_up_screen")
    data object HomeScreen: Screen("home_screen")
    data object Detail : Screen("detail_screen/{productId}"){
        fun createRoute(productId: Int) = "detail_screen/$productId"
    }

    data object Bookmarks: Screen("bookmarks_screen")
    data object Profile: Screen("profile_screen")
    data object Favourites: Screen("favourites_screen")
    data object Onboarding: Screen("onboarding_screen")
    data object Otp: Screen("otp_screen")
    data object ProductSearch: Screen("product_search_screen")
    data object AllProductsList: Screen("all_products_list_screen")
    data object ChatListScreen: Screen("chat_list_screen")
    data object ChatScreen : Screen("chat_screen/{chatId}/{userId?}") {
        fun createRoute(chatId: String, userId: String? = null): String {
            return if (userId != null) "chat_screen/$chatId/$userId"
            else "chat_screen/$chatId"
        }
    }


    object BottomNavItems {
        val items = listOf(
            HomeScreen,
            ChatListScreen,
            Bookmarks,
            Favourites,
            Profile,
        )
    }
}