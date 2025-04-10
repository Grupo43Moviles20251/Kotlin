package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.dto.UserDto
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val backendServiceAdapter: BackendServiceAdapter
) {
    suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String,
        address: String,
        birthday: String,
        callback: (Boolean, String) -> Unit
    ) {
        val userDto = UserDto(name, email, password, address, birthday)
        backendServiceAdapter.registerUserWithEmail(userDto) { success, error ->
            if (success) {
                callback(true, "User registered successfully!")
            } else {
                callback(false, error ?: "Sign-up failed")
            }
        }
    }
}