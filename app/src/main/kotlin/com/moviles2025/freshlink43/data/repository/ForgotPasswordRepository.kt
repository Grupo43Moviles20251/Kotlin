package com.moviles2025.freshlink43.data.repository

import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordRepository {

    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            callback(false, "Email field is empty.")
            return
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Check your email for a reset link.")
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: "Error sending reset email."
                    callback(false, errorMsg)
                }
            }
    }
}