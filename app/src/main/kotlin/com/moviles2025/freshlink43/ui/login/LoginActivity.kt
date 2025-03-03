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

        // Configurar el texto "Forgot your password?" con spannable
        setupForgotPasswordText(forgotPasswordTextView)

        // Acción de login (solo mock para ahora)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Toast.makeText(this, "Logging in with $email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupForgotPasswordText(forgotPasswordTextView: TextView) {
        val fullText = "Forgot your password?"
        val spannable = SpannableString(fullText)

        val start = fullText.indexOf("password")
        val end = start + "password".length

        // Hacer "password" en negrita
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Hacer "password" clickable
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@LoginActivity, "Redirect to recover password", Toast.LENGTH_SHORT).show()
                // Aquí puedes abrir un fragmento, otra Activity o lanzar un intent
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                //ds.color = forgotPasswordTextView.currentTextColor pa quitar que se vea brillante
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        forgotPasswordTextView.text = spannable
        forgotPasswordTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }
}