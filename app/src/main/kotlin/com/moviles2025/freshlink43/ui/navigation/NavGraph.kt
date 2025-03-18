package com.moviles2025.freshlink43.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moviles2025.freshlink43.ui.home.HomeScreen
import com.moviles2025.freshlink43.ui.profile.ProfileScreen
import com.moviles2025.freshlink43.ui.ubication.UbicationScreen
import com.moviles2025.freshlink43.ui.login.LoginScreen
import com.moviles2025.freshlink43.ui.signup.SignUpScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        //composable("login") { LoginScreen(navController) }
        //composable("signup") { SignUpScreen(navController) }
        //composable("home") { HomeScreen(navController) }
        //composable("profile") { ProfileScreen(navController) }
       //composable("ubication") { UbicationScreen(navController) }
    }
}