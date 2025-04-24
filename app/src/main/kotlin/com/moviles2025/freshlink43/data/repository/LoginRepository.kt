package com.moviles2025.freshlink43.data.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import com.moviles2025.freshlink43.data.mappers.toDto

class LoginRepository(
    private val firebaseServiceAdapter: FirebaseServiceAdapter,
    private val backendServiceAdapter: BackendServiceAdapter
) {

    fun loginWithEmail(email: String, password: String, context: Context, callback: (Boolean, String) -> Unit) {
        firebaseServiceAdapter.signInWithEmail(email, password) { idToken, error: String? ->
            if (idToken != null) {
                verifyUserWithBackend(idToken, context, callback)
            } else {
                callback(false, error ?: "Unknown error")
            }
        }
    }

    fun loginWithGoogle(credential: AuthCredential, context: Context, callback: (Boolean, String) -> Unit) {
        firebaseServiceAdapter.signInWithGoogle(credential) { idToken, error ->
            if (idToken != null) {
                verifyUserWithBackend(idToken, context) { success, message ->
                    if (!success) {
                        registerUserWithGoogle(idToken, context, callback)
                    } else {
                        callback(true, "Login successful!")
                    }
                }
            } else {
                callback(false, error ?: "Unknown error")
            }
        }
    }

    private fun verifyUserWithBackend(idToken: String, context: Context, callback: (Boolean, String) -> Unit) {
        backendServiceAdapter.verifyUser(idToken) { success, errorMessage ->
            if (success) {
                firebaseServiceAdapter.getCurrentUser()?.uid?.let {
                    firebaseServiceAdapter.registerDeviceInfo(it)
                }
                callback(true, "Login successful!")
            } else {
                callback(false, errorMessage ?: "Verification failed")
            }
        }
    }

    private fun registerUserWithGoogle(idToken: String, context: Context, callback: (Boolean, String) -> Unit) {
        val user = firebaseServiceAdapter.getCurrentUser()
        if (user == null) {
            callback(false, "No authenticated user found")
            return
        }



        val userDto = user.toDto()

        backendServiceAdapter.registerUser(userDto, idToken) { success, errorMessage ->  // ✅ Ahora sí manda un UserDto
            if (success) {
                callback(true, "User registered successfully!")
            } else {
                callback(false, errorMessage ?: "Sign-up failed")
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}