package com.moviles2025.freshlink43.ui.login

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.TextPaint
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.moviles2025.freshlink43.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)

        // Configurar el texto "Forgot your password?" con spannable
        setupForgotPasswordText(forgotPasswordTextView)
        setupSignUpText(signUpTextView)

        // Acción de login (solo mock para ahora)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Toast.makeText(this, "Logging in with $email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupForgotPasswordText(forgotPasswordTextView: TextView) {
        val textPassword = "Forgot your password?"
        val spannablePassword = SpannableString(textPassword)

        val startPassword = textPassword.indexOf("password")
        val endPassword = startPassword + "password".length

        // Hacer "password" en negrita
        spannablePassword.setSpan(StyleSpan(Typeface.BOLD), startPassword, endPassword, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Hacer "password" clickable
        spannablePassword.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@LoginActivity, "Redirect to recover password", Toast.LENGTH_SHORT).show()
                // Aquí puedes abrir un fragmento, otra Activity o lanzar un intent
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, startPassword, endPassword, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        forgotPasswordTextView.text = spannablePassword
        forgotPasswordTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }

    private fun setupSignUpText(signUpTextView: TextView) {
        val textSignIn = "Sign Up"
        val spannableSignUp = SpannableString(textSignIn)

        val startSignUp = textSignIn.indexOf("Sign Up")
        val endSingUp = startSignUp + "Sign Up".length

        spannableSignUp.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@LoginActivity, "Redirect to Sign Up", Toast.LENGTH_SHORT).show()
                // Aquí puedes abrir un fragmento, otra Activity o lanzar un intent
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = signUpTextView.currentTextColor
            }
        }, startSignUp, endSingUp, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        signUpTextView.text = spannableSignUp
        signUpTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}