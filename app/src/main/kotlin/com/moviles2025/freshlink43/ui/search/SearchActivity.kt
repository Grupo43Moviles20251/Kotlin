package com.moviles2025.freshlink43.ui.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.moviles2025.freshlink43.ui.profile.ProfileActivity

class SearchActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el contenido de la vista con el composable SearchScreen
        setContent {
            SearchScreen(
                viewModel = searchViewModel,
                onNavigateToProfile = {
                    // Navegar al perfil cuando se haga clic en el icono
                    startActivity(Intent(this, ProfileActivity::class.java))
                },
                onSearchClick = {
                    startActivity(Intent(this, SearchActivity::class.java)) // Redirigir a SearchActivity
                }
            )
        }

        // Ocultar la barra de navegación
        window.decorView.post {
            hideSystemUI()
        }
    }

    // Manejar el comportamiento del botón de retroceso
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    // Función para ocultar las barras del sistema
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}
