package com.moviles2025.freshlink43.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import com.moviles2025.freshlink43.ui.profile.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseServiceAdapter: FirebaseServiceAdapter
) {

    suspend fun getUserProfile(): Result<UserProfile?> {
        val userId = firebaseServiceAdapter.getCurrentUser()?.uid
            ?: return Result.failure(Exception("User not logged in"))

        return try {
            val doc = FirebaseFirestore.getInstance().collection("users").document(userId).get().await()
            if (doc.exists()) {
                Result.success(doc.toObject(UserProfile::class.java))
            } else {
                Result.failure(Exception("Profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProfileImage(imageUri: Uri): Result<String> {
        return firebaseServiceAdapter.uploadUserProfileImage(imageUri)
    }

    fun signOut() {
        firebaseServiceAdapter.signOut()
    }
}