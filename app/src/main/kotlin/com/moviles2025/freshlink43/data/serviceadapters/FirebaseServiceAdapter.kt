package com.moviles2025.freshlink43.data.serviceadapters

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.dto.RestaurantMaps
import kotlinx.coroutines.tasks.await

class FirebaseServiceAdapter {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signInWithEmail(
        email: String,
        password: String,
        callback: (String?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            callback(idToken, null)
                        } else {
                            callback(null, tokenTask.exception?.localizedMessage)
                        }
                    }
                } else {
                    callback(null, task.exception?.localizedMessage)
                }
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

    fun signInWithGoogle(
        credential: AuthCredential,
        callback: (String?, String?) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val idToken = tokenTask.result?.token
                            callback(idToken, null)
                        } else {
                            callback(null, tokenTask.exception?.localizedMessage)
                        }
                    }
                } else {
                    callback(null, task.exception?.localizedMessage)
                }
            }
    }

    suspend fun getRestaurantsFromFirestore(): List<RestaurantMaps> {
        return try {
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