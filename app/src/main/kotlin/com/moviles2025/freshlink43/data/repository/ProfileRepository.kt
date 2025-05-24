package com.moviles2025.freshlink43.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import com.moviles2025.freshlink43.ui.profile.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseServiceAdapter: FirebaseServiceAdapter
) {

    suspend fun updateUserProfile(profile: UserProfile) = firebaseServiceAdapter.updateUserProfile(profile)

    suspend fun getUserProfile(source: Source = Source.DEFAULT): Result<UserProfile?> {
        return firebaseServiceAdapter.getUserProfile(source)
    }

    suspend fun uploadProfileImage(imageUri: Uri): Result<String> {
        return firebaseServiceAdapter.uploadUserProfileImage(imageUri)
    }

    fun signOut() {
        firebaseServiceAdapter.signOut()
    }
}