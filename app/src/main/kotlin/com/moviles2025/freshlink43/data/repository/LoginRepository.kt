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

    suspend fun loginWithEmail(email: String, password: String): Result<String> {
        val (idToken, error) = firebaseServiceAdapter.signInWithEmail(email, password)

        if (idToken == null) {
            return Result.failure(Exception(error ?: "Login failed"))
        }

        val verification = verifyUserWithBackend(idToken)

        return if (verification.isSuccess) {
            Result.success("Login successful!")
        } else {
            Result.failure(verification.exceptionOrNull() ?: Exception("Backend verification failed"))
        }
    }

    suspend fun loginWithGoogle(credential: AuthCredential): Result<String> {
        return try {
            val (idToken, error) = firebaseServiceAdapter.signInWithGoogle(credential)
            if (idToken == null) return Result.failure(Exception(error ?: "Google Sign-In failed"))

            val verification = verifyUserWithBackend(idToken)
            if (verification.isSuccess) {
                Result.success("Login successful!")
            } else {
                // usuario no existe, registrar
                val user = firebaseServiceAdapter.getCurrentUser()
                    ?: return Result.failure(Exception("No authenticated user found"))

                val userDto = user.toDto()
                val registration = backendServiceAdapter.registerUser(userDto, idToken)

                if (registration.isSuccess) {
                    Result.success("User registered successfully!")
                } else {
                    Result.failure(registration.exceptionOrNull() ?: Exception("Registration failed"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyUserWithBackend(idToken: String): Result<Boolean> {
        val result = backendServiceAdapter.verifyUser(idToken)

        return if (result.isSuccess) {
            val userId = firebaseServiceAdapter.getCurrentUser()?.uid
            userId?.let { firebaseServiceAdapter.registerDeviceInfo(it) }
            Result.success(true)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Verification failed"))
        }
    }

    suspend fun registerUserWithGoogle(idToken: String): Result<String> {
        val user = firebaseServiceAdapter.getCurrentUser()
            ?: return Result.failure(Exception("No authenticated user found"))

        val userDto = user.toDto()

        val result = backendServiceAdapter.registerUser(userDto, idToken)
        return if (result.isSuccess) {
            Result.success("User registered successfully!")
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Sign-up failed"))
        }
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}