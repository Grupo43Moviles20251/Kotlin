package com.moviles2025.freshlink43.ui.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Main : NavRoutes("main")
    object Home : NavRoutes("home")
    object Profile : NavRoutes("profile")
    object Search : NavRoutes("search")
    object Ubication : NavRoutes("ubication")
    object Login : NavRoutes("login")
    object SignUp : NavRoutes("signup")
    object ForgotPassword : NavRoutes("forgotpass")
    object Favorites : NavRoutes("favorites")
    object Recommendations : NavRoutes("recommendations")
    object Detail : NavRoutes("detail/{productId}")
    object Order : NavRoutes("order")
}
