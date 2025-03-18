package com.moviles2025.freshlink43.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.moviles2025.freshlink43.ui.login.LoginActivity
import com.moviles2025.freshlink43.ui.profile.ProfileActivity

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            // Si no hay usuario autenticado, redirigir al login
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Evita que vuelva atrás con el botón de retroceso
            return
        }

        setContent {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToProfile = {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            )
        }
        window.decorView.post {
            hideSystemUI()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

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