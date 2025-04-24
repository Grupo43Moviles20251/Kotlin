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
        birthday: String
    ): Result<String> {
        val userDto = UserDto(name, email, password, address, birthday)
        val result = backendServiceAdapter.registerUserWithEmail(userDto)

        return if (result.isSuccess) {
            Result.success("User registered successfully!")
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Sign-up failed"))
        }
    }
}