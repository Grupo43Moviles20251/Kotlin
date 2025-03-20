package com.moviles2025.freshlink43

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.moviles2025.freshlink43.ui.navigation.NavGraph
import com.moviles2025.freshlink43.ui.utils.FreshLinkTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FreshLinkTheme(darkTheme = false) {
                val navController = rememberNavController()
                NavGraph(navController) // 🔹 Llamamos a la navegación aquí
            }
        }
    }
}