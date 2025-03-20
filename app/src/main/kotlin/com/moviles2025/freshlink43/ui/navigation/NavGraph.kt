package com.moviles2025.freshlink43.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moviles2025.freshlink43.ui.forgotpass.ForgotPasswordScreen
import com.moviles2025.freshlink43.ui.forgotpass.ForgotPasswordViewModel
import com.moviles2025.freshlink43.ui.home.HomeScreen
import com.moviles2025.freshlink43.ui.home.HomeViewModel
import com.moviles2025.freshlink43.ui.login.LoginScreen
import com.moviles2025.freshlink43.ui.login.LoginViewModel
import com.moviles2025.freshlink43.ui.main.MainScreen
import com.moviles2025.freshlink43.ui.profile.ProfileScreen
import com.moviles2025.freshlink43.ui.search.SearchScreen
import com.moviles2025.freshlink43.ui.search.SearchViewModel
import com.moviles2025.freshlink43.ui.signup.SignUpScreen
import com.moviles2025.freshlink43.ui.signup.SignUpViewModel
import com.moviles2025.freshlink43.ui.ubication.UbicationScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Main.route // Pantalla de inicio
    ) {
        composable(NavRoutes.Main.route) { MainScreen(navController) }
        composable(NavRoutes.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        //composable(NavRoutes.Profile.route) { ProfileScreen() }
        composable(NavRoutes.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(NavRoutes.Ubication.route) { UbicationScreen() }

        composable(NavRoutes.ForgotPassword.route) {
            val viewModel: ForgotPasswordViewModel = hiltViewModel()
            ForgotPasswordScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(NavRoutes.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(NavRoutes.SignUp.route) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}