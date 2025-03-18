package com.moviles2025.freshlink43.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.forgotpass.ForgotPasswordActivity
import com.moviles2025.freshlink43.ui.home.HomeActivity
import com.moviles2025.freshlink43.ui.signup.SignUpActivity
import com.moviles2025.freshlink43.ui.utils.FreshLinkTheme

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FreshLinkTheme (darkTheme = false){
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToSignUp = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                        finish()
                    },
                    onLoginSuccess = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onGoogleSignIn = { launchGoogleSignIn() },
                    onNavigateToForgotPassword = {
                        startActivity(Intent(this, ForgotPasswordActivity::class.java))
                        finish()
                    }
                )
            }
        }

        if (intent.getBooleanExtra("signup_success", false)) {
            Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_LONG).show()
        }

        setupGoogleSignIn()
    }

    private fun setupGoogleSignIn() {
        val webClientId = getString(R.string.default_web_client_id)

        if (webClientId.isEmpty()) {
            Log.e("GoogleSignIn", "ERROR: default_web_client_id está vacío o nulo")
        } else {
            Log.d("GoogleSignIn", "Client ID: $webClientId")
        }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, options)
    }

    private fun launchGoogleSignIn() {
        // 🔥 Cerrar sesión antes de iniciar sesión para forzar el selector de cuentas
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                Log.d("GoogleSignIn", "✅ Cuenta seleccionada: ${account.email}") // Verificar en Logcat
                viewModel.loginWithGoogle(credential, this)

            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "❌ Google Sign-In falló: ${e.statusCode}")
                Toast.makeText(this, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}