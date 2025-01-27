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
    data object NewChat : Screen("new_chat_screen")
    data object ChatScreen : Screen("chat_screen/{chatId}/{contactName}/{currentUser}") {
        fun createRoute(chatId: String, contactName: String, currentUser: String): String {
            return "chat_screen/$chatId/${contactName}/${currentUser}"
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