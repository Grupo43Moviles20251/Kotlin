package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.model.LoginResult

class LoginRepository {

    fun login(email: String, password: String): LoginResult {
        return if (email == "test@example.com" && password == "123456") {
            LoginResult(success = true)
        } else {
            LoginResult(success = false, message = "Invalid credentials")
        }
    }
}