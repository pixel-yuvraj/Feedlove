package com.feedlove.app.navigation

sealed class Screen(val route: String) {
    object Splash    : Screen("splash")
    object Login     : Screen("login")
    object SignUp    : Screen("signup")
    object Home      : Screen("home")         // ← Make sure this exists!
    object ShareFood : Screen("share_food")
    object BorrowFood: Screen("borrow_food")
    object Profile   : Screen("profile")      // only used for deep‑links, not the bottom bar
    object Settings  : Screen("settings")
    object Chat       : Screen("chat")
    object PrivacyPolicy : Screen("privacy_policy")


}
