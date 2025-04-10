package com.moviles2025.freshlink43.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.navigation.NavRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1000 // 1 segundo de animación
        ), label = "scaleAnimation"
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2000) // 2 segundos para mostrar splash
        if (firebaseAuth.currentUser != null) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(NavRoutes.Main.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(scale) // Aplica la animación de escala
        )
    }
}