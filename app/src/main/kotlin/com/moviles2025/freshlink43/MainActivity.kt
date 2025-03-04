package com.moviles2025.freshlink43

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToLoginButton = findViewById<Button>(R.id.logInButton)
        val goToSignUpButton = findViewById<Button>(R.id.signUpButton)

        goToLoginButton.setOnClickListener {
            val intent = Intent(this, com.moviles2025.freshlink43.ui.login.LoginActivity::class.java)
            startActivity(intent)
        }

        goToSignUpButton.setOnClickListener {
            val intent = Intent(this, com.moviles2025.freshlink43.ui.signup.SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}