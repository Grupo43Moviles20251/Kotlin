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
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Source
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.ui.profile.UserProfile
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    suspend fun getTopRestaurantsFromVisitsThisMonth(monthYear: String): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val visitsCollection = firestore.collection("restaurant_visits")

            // monthYear = "2025-05" (yyyy-MM) para filtrar documentos que empiecen con esa fecha
            val snapshot = visitsCollection
                .whereGreaterThanOrEqualTo(FieldPath.documentId(), "$monthYear-01")
                .whereLessThanOrEqualTo(FieldPath.documentId(), "$monthYear-31")
                .get()
                .await()

            val visitsCount = mutableMapOf<String, Int>()

            for (doc in snapshot.documents) {
                val data = doc.data ?: continue
                for ((key, value) in data) {
                    if (key == "last_visited_by") continue
                    val count = (value as? Number)?.toInt() ?: 0
                    visitsCount[key] = visitsCount.getOrDefault(key, 0) + count
                }
            }

            Log.d("TopRestaurants", "Visitas totales en $monthYear: $visitsCount")

            // Top 3 o 4 restaurantes con más visitas
            val topRestaurants = visitsCount.entries
                .sortedByDescending { it.value }
                .take(3)
                .map { it.key }

            Log.d("TopRestaurants", "Top restaurantes en $monthYear: $topRestaurants")
            Result.success(topRestaurants)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRestaurantsByNames(names: List<String>): Result<List<Restaurant>> = withContext(Dispatchers.IO) {
        try {
            if (names.isEmpty()) return@withContext Result.success(emptyList())

            val restaurantsSnapshot = firestore.collection("retaurants")
                .whereIn("name", names)
                .get()
                .await()

            val restaurants = restaurantsSnapshot.documents.mapNotNull { it.toObject(Restaurant::class.java) }
            Result.success(restaurants)
        } catch (e: Exception) {
            Result.failure(e)
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

    suspend fun getUserProfile(
        source: Source = Source.DEFAULT
    ): Result<UserProfile?> = withContext(Dispatchers.IO) {
        val uid = getCurrentUser()?.uid
            ?: return@withContext Result.failure(Exception("User not logged in"))
        return@withContext try {
            val snap = firestore
                .collection("users")
                .document(uid)
                .get(source)     // aquí pasamos el source
                .await()
            if (snap.exists()) {
                Result.success(snap.toObject(UserProfile::class.java))
            } else {
                Result.failure(Exception("Profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(updated: UserProfile): Result<Void?> =
        withContext(Dispatchers.IO) {
            val uid = getCurrentUser()?.uid
                ?: return@withContext Result.failure(Exception("User not logged in"))
            try {
                firestore.collection("users")
                    .document(uid)
                    .set(updated)
                    .await()
                Result.success(null)
            } catch (e: Exception) {
                Result.failure(e)
            }
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



    suspend fun uploadUserProfileImage(imageUri: Uri): Result<String> {
        val uid = getCurrentUser()?.uid
            ?: return Result.failure(Exception("No user logged in"))

        return try {
            val storageRef = FirebaseStorage.getInstance().reference
                .child("profile_images/$uid.jpg")

            // Subir archivo
            storageRef.putFile(imageUri).await()

            // Obtener URL de descarga
            val url = storageRef.downloadUrl.await().toString()

            // Actualizar el campo photoUrl en Firestore
            firestore.collection("users")
                .document(uid)
                .update("photoUrl", url)
                .await()

            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}