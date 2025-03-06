package com.moviles2025.freshlink43.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.moviles2025.freshlink43.ui.profile.ProfileActivity

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToProfile = {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
            )
        }
    }

//prueba
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}