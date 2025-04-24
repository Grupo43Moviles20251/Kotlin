package com.moviles2025.freshlink43.data.serviceadapters

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.dto.RestaurantMaps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseServiceAdapter {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun signInWithEmail(email: String, password: String): Pair<String?, String?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val tokenResult = authResult.user?.getIdToken(true)?.await()
            val idToken = tokenResult?.token
            Pair(idToken, null)
        } catch (e: Exception) {
            Pair(null, e.localizedMessage)
        }
    }

    fun sendPasswordResetEmail(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: "Error sending reset email."
                    callback(false, errorMsg)
                }
            }
    }

    suspend fun signInWithGoogle(credential: AuthCredential): Pair<String?, String?> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            val tokenResult = authResult.user?.getIdToken(true)?.await()
            val idToken = tokenResult?.token
            Pair(idToken, null)
        } catch (e: Exception) {
            Pair(null, e.localizedMessage)
        }
    }

    suspend fun getRestaurantsFromFirestore(): List<RestaurantMaps> = withContext(Dispatchers.IO) {
        try {
            val snapshot = firestore.collection("retaurants").get().await()
            snapshot.documents.mapNotNull { it.toObject(RestaurantMaps::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseServiceAdapter", "Error fetching restaurants: ${e.message}")
            emptyList()
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }

    fun registerDeviceInfo(userId: String) {
        val model = android.os.Build.MODEL ?: "Unknown"
        val brand = android.os.Build.BRAND ?: "Unknown"
        val osVersion = android.os.Build.VERSION.RELEASE ?: "Unknown"

        val deviceData = mapOf(
            "userId" to userId,
            "model" to model,
            "brand" to brand,
            "osVersion" to osVersion,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        firestore.collection("userDevices")
            .document(userId) // sobrescribe si ya existe, no duplica
            .set(deviceData)
            .addOnSuccessListener {
                Log.d("DeviceInfo", "Device info saved for user: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("DeviceInfo", "Failed to save device info: ${e.localizedMessage}")
            }
    }
}