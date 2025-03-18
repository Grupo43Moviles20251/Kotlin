package com.moviles2025.freshlink43.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.moviles2025.freshlink43.ui.login.LoginActivity
import com.moviles2025.freshlink43.ui.utils.FreshLinkTheme

class SignUpActivity : ComponentActivity() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshLinkTheme (darkTheme = false) {
                SignUpScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}