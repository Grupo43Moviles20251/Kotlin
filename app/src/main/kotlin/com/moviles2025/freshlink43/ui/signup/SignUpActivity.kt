package com.moviles2025.freshlink43.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.moviles2025.freshlink43.R


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextInputEditText>(R.id.confirmPasswordEditText)

        val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.confirmPasswordLayout)
        val logInTextView = findViewById<TextView>(R.id.logInTextView)

        // Si quieres prellenar (ejemplo)
        emailEditText.setText("example@freshlink.com")
        setupToLoginText(logInTextView)

        findViewById<android.widget.Button>(R.id.signUpButton).setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password != confirmPassword) {
                confirmPasswordLayout.error = "Passwords do not match"
            } else {
                confirmPasswordLayout.error = null
                Toast.makeText(this, "Account created for $email", Toast.LENGTH_SHORT).show()
                // Aquí lanzas tu lógica real (enviar al ViewModel, al repo, etc.)
            }
        }
    }

    private fun setupToLoginText(logInTextView: TextView) {
        val textSignIn = "Log In"
        val spannableLogin = SpannableString(textSignIn)

        val startLogIn = textSignIn.indexOf("Log In")
        val endLogIn = startLogIn + "Log In".length

        spannableLogin.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@SignUpActivity, "Redirect log In", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, com.moviles2025.freshlink43.ui.login.LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = logInTextView.currentTextColor
            }
        }, startLogIn, endLogIn, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        logInTextView.text = spannableLogin
        logInTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}