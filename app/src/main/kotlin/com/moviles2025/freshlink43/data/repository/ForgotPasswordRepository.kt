package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import javax.inject.Inject

class ForgotPasswordRepository @Inject constructor(
    private val firebaseServiceAdapter: FirebaseServiceAdapter
) {
    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            callback(false, "Email field is empty.")
            return
        }

        firebaseServiceAdapter.sendPasswordResetEmail(email) { success, errorMsg ->
            if (success) {
                callback(true, "Check your email for a reset link.")
            } else {
                callback(false, errorMsg ?: "Error sending reset email.")
            }
        }
    }
}