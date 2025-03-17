package com.moviles2025.freshlink43.data.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginRepository {



    private val auth = FirebaseAuth.getInstance()
    private val client = OkHttpClient()

    fun loginWithEmail(
        email: String,
        password: String,
        context: Context,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            if (idToken != null) {
                                verifyUserWithBackend(idToken, context, callback)
                            } else {
                                callback(false, "Failed to get token")
                            }
                        } else {
                            callback(false, "Failed to get token")
                        }
                    }
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Login failed")
                }
            }
    }

    fun loginWithGoogle(credential: AuthCredential, context: Context, callback: (Boolean, String) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            Log.d("GoogleSignIn", "Token obtenido: $idToken")
                            if (idToken != null) {
                                // 🔍 Verificar si el usuario ya existe en Firestore a través del backend
                                verifyUserWithBackend(idToken, context) { success, message ->
                                    if (!success) {
                                        // 🔹 Si el usuario no existe en Firestore, lo registramos con Google Sign Up
                                        registerUserWithGoogle(idToken, context, callback)
                                    } else {
                                        callback(success, message) // Usuario encontrado, inicia sesión normal
                                    }
                                }
                            } else {
                                callback(false, "Failed to get token")
                            }
                        } else {
                            callback(false, "Failed to get token")
                        }
                    }
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Google Sign-In failed")
                }
            }
    }

    private fun verifyUserWithBackend(idToken: String, context: Context, callback: (Boolean, String) -> Unit) {
        val request = Request.Builder()
            .url("http://192.168.1.111:8000/users/me") // Verificar si el usuario existe
            .get()
            .addHeader("Authorization", "Bearer $idToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Backend", "Error al conectar con el backend: ${e.localizedMessage}")
                callback(false, "Backend error: ${e.localizedMessage}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Backend", "Respuesta al verificar usuario: $responseBody")

                if (response.isSuccessful) {
                    Log.d("Backend", "✅ Usuario encontrado en Firestore")
                    callback(true, "Login successful!")
                } else if (response.code == 404) {
                    Log.d("Backend", " Usuario no encontrado en Firestore. Registrando...")
                    registerUserWithGoogle(idToken, context, callback)
                } else {
                    val errorMessage = responseBody ?: "Unknown error"
                    Log.e("Backend", "⚠ Error inesperado en la verificación: $errorMessage")
                    callback(false, "Login failed: $errorMessage")
                }
            }
        })
    }
    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun registerUserWithGoogle(idToken: String, context: Context, callback: (Boolean, String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            callback(false, "No authenticated user found")
            return
        }


        val email = user.email ?: ""
        val name = user.displayName ?: "Unknown"
        val photoUrl = user.photoUrl?.toString() ?: ""



        val json = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", "google_auth")  // No se usa realmente, solo para cumplir el esquema
            put("address", "Unknown")
            put("birthday", "2000-01-01")  // Valor por defecto si el backend lo requiere
            put("photoUrl", photoUrl)  // Enviar la foto de perfil si el backend la acepta
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://192.168.1.111:8000/signup") // Endpoint para registrar usuarios
            .post(requestBody)
            .addHeader("Authorization", "Bearer $idToken") // Enviar el token de Firebase
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GoogleSignIn", "❌ Error al registrar usuario: ${e.localizedMessage}")
                callback(false, "Failed to register user: ${e.localizedMessage}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("GoogleSignIn", "📨 Respuesta del backend al registrar usuario: $responseBody")

                if (response.isSuccessful) {
                    callback(true, "User registered successfully!")
                } else {
                    val errorMessage = responseBody ?: "Unknown error"
                    Log.e("GoogleSignIn", "⚠ Error en el registro: $errorMessage")
                    callback(false, "Sign-up failed: $errorMessage")
                }
            }
        })
    }
}