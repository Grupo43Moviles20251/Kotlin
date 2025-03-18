package com.moviles2025.freshlink43.ui.forgotpass

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.moviles2025.freshlink43.ui.login.LoginActivity
import com.moviles2025.freshlink43.ui.utils.FreshLinkTheme

class ForgotPasswordActivity : AppCompatActivity() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshLinkTheme (darkTheme = false){
                ForgotPasswordScreen(
                    viewModel = viewModel,
                    onBackToLogin = {

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}