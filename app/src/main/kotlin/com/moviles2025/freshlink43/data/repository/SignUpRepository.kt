package com.moviles2025.freshlink43.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SignUpRepository {

    private val auth = FirebaseAuth.getInstance()

    fun signUpWithEmail(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Account created successfully!")
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Sign up failed")
                }
            }
    }
}