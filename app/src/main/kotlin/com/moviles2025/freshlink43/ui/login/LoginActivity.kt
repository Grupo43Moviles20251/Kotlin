package com.moviles2025.freshlink43.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.home.HomeActivity
import com.moviles2025.freshlink43.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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
                onGoogleSignIn = { launchGoogleSignIn() }
            )
        }

        if (intent.getBooleanExtra("signup_success", false)) {
            Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_LONG).show()
        }
    }

    private fun launchGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, options)
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.loginWithGoogle(credential)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}