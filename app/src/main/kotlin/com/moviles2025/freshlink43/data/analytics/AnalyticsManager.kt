package com.moviles2025.freshlink43.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.util.*

object AnalyticsManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun logFeatureUsage(featureName: String) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val data = hashMapOf(
            "feature_name" to featureName,
            "user_id" to userId,
            "timestamp" to Date()
        )

        db.collection("feature_usage")
            .add(data)
            .addOnSuccessListener {
                // Registro exitoso
            }
            .addOnFailureListener { e ->
                // Manejo de error si falla
            }
    }
}