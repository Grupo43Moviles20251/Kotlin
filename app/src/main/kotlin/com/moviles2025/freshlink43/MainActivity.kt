package com.moviles2025.freshlink43

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.moviles2025.freshlink43.ui.navigation.NavGraph
import com.moviles2025.freshlink43.utils.FreshLinkTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = getColor(R.color.white) // Usa el color de fondo correcto
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            FreshLinkTheme(darkTheme = false) {
                val navController = rememberNavController()
                NavGraph(navController) // 🔹 Llamamos a la navegación aquí
            }
        }
    }
}