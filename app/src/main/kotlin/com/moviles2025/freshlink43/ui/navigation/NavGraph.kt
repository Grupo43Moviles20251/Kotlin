package com.moviles2025.freshlink43.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.moviles2025.freshlink43.ui.favorites.FavoritesViewModel
import com.moviles2025.freshlink43.ui.forgotpass.ForgotPasswordScreen
import com.moviles2025.freshlink43.ui.forgotpass.ForgotPasswordViewModel
import com.moviles2025.freshlink43.ui.home.HomeScreen
import com.moviles2025.freshlink43.ui.home.HomeViewModel
import com.moviles2025.freshlink43.ui.login.LoginScreen
import com.moviles2025.freshlink43.ui.login.LoginViewModel
import com.moviles2025.freshlink43.ui.main.MainScreen
import com.moviles2025.freshlink43.ui.search.SearchScreen
import com.moviles2025.freshlink43.ui.search.SearchViewModel
import com.moviles2025.freshlink43.ui.signup.SignUpScreen
import com.moviles2025.freshlink43.ui.signup.SignUpViewModel
import com.moviles2025.freshlink43.ui.maps.UbicationScreen
import com.moviles2025.freshlink43.ui.maps.UbicationViewModel
import com.moviles2025.freshlink43.ui.profile.ProfileScreen
import com.moviles2025.freshlink43.ui.profile.ProfileViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    var isUserAuthenticated by remember { mutableStateOf(firebaseAuth.currentUser != null) }
    val user = FirebaseAuth.getInstance().currentUser
    // Escuchar cambios de autenticación en tiempo real
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(NavRoutes.Main.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Main.route // Siempre inicia en Main
    ) {
        composable(NavRoutes.Main.route) { MainScreen(navController) }

        composable(NavRoutes.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, viewModel)
        }

        composable(NavRoutes.SignUp.route) {
            val viewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(navController, viewModel)
        }

        composable(NavRoutes.ForgotPassword.route) {
            val viewModel: ForgotPasswordViewModel = hiltViewModel()
            ForgotPasswordScreen(navController, viewModel)
        }

        // 🔹 Pantallas protegidas (requieren autenticación)
        composable(NavRoutes.Home.route) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(navController, viewModel)
            } else {
                LaunchedEffect(Unit) { navController.navigate(NavRoutes.Main.route) }
            }
        }

/*
        composable(NavRoutes.Favorites.route) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val viewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(navController, viewModel)
            } else {
                LaunchedEffect(Unit) { navController.navigate(NavRoutes.Main.route) }
            }
        }

 */

        composable(NavRoutes.Search.route) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(navController, viewModel)
            } else {
                LaunchedEffect(Unit) { navController.navigate(NavRoutes.Main.route) }
            }
        }

        composable(NavRoutes.Ubication.route) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val viewModel: UbicationViewModel = hiltViewModel()
                UbicationScreen(navController, viewModel)
            } else {
                LaunchedEffect(Unit) { navController.navigate(NavRoutes.Main.route) }
            }
        }

        composable(NavRoutes.Profile.route) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(navController, viewModel)
            } else {
                LaunchedEffect(Unit) { navController.navigate(NavRoutes.Main.route) }
            }
        }
    }
}