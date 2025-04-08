package com.moviles2025.freshlink43.data.services

import android.content.Context
import android.widget.Toast
import com.moviles2025.freshlink43.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class SignUpService {

    private val client = OkHttpClient()

    suspend fun signUpWithEmail(
        context: Context,
        name: String,
        email: String,
        password: String,
        address: String,
        birthday: String,
        callback: (Boolean, String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {

                val json = JSONObject().apply {
                    put("name", name)
                    put("email", email)
                    put("password", password)
                    put("address", address)
                    put("birthday", birthday)
                }

                val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url("${Constants.BASE_URL}/signup")
                    .post(requestBody)
                    .header("Content-Type", "application/json")
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    callback(true, "User registered successfully!")
                } else {
                    val errorMessage = response.body?.string() ?: "Unknown error"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    callback(false, errorMessage)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Sign-up Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                callback(false, "Sign-up Error: ${e.message}")
            }
        }
    }
}