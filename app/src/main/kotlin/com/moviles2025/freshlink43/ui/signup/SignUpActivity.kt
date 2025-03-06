package com.moviles2025.freshlink43.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.confirmPasswordEditText)
        val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.confirmPasswordLayout)
        val logInTextView = findViewById<TextView>(R.id.logInTextView)

        setupToLoginText(logInTextView)

        findViewById<android.widget.Button>(R.id.signUpButton).setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordLayout.error = "Passwords do not match"
            } else {
                confirmPasswordLayout.error = null
                viewModel.signUp(email, password)
            }
        }

        viewModel.signUpResult.observe(this) { (success, message) ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (success) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("signup_success", true)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupToLoginText(logInTextView: TextView) {
        val text = "Log In"
        val spannable = SpannableString(text)

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: android.view.View) {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = logInTextView.currentTextColor
            }
        }, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        logInTextView.text = spannable
        logInTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}