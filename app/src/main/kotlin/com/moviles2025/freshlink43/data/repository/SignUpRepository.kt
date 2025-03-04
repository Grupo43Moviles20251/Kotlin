package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.model.SignUpResult
import kotlinx.coroutines.delay

class SignUpRepository {

    suspend fun registerUser(email: String, password: String): SignUpResult {
        // Simulación de una llamada a un backend (por ahora solo delay)
        delay(2000)

        // Por ahora, simulamos que cualquier email que termine en @freshlink.com es exitoso
        return if (email.endsWith("@freshlink.com")) {
            SignUpResult(success = true, message = "Welcome to FreshLink!")
        } else {
            SignUpResult(success = false, message = "Invalid email. Please use a freshlink.com account.")
        }
    }
}