package com.moviles2025.freshlink43.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class LoginRepository {

    private val auth = FirebaseAuth.getInstance()

    fun loginWithEmail(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful!")
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Login failed")
                }
            }
    }

    fun loginWithGoogle(credential: AuthCredential, callback: (Boolean, String) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Google Sign-In successful!")
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Google Sign-In failed")
                }
            }
    }
}