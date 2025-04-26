package com.moviles2025.freshlink43.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*

object AnalyticsManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun logFeatureUsage(featureName: String) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val usageRef = db.collection("feature_usage").document(today)

        usageRef.get().addOnSuccessListener { document ->
            val currentCount = document.getLong(featureName) ?: 0


            val updateData = mapOf(
                featureName to currentCount + 1,
                "last_used_by" to userId  // Último usuario que usó la función
            )

            usageRef.set(updateData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("Analytics", "Uso de $featureName registrado exitosamente")
                }
                .addOnFailureListener { e ->
                    Log.e("Analytics", "Error registrando uso: ${e.message}")
                }

        }.addOnFailureListener { e ->
            Log.e("Analytics", "Error obteniendo documento de Firestore: ${e.message}")
        }
    }
    fun logRestaurantVisit(restaurantName: String) {
        val userId = auth.currentUser?.uid ?: "anonymous"
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val restaurantRef = db.collection("restaurant_visits").document(today)

        restaurantRef.get().addOnSuccessListener { document ->
            val currentCount = document.getLong(restaurantName) ?: 0

            val updateData = mapOf(
                restaurantName to currentCount + 1, // Incrementa el contador
                "last_visited_by" to userId  // Último usuario que visitó el restaurante
            )

            restaurantRef.set(updateData, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("Analytics", "Visita al restaurante $restaurantName registrada exitosamente")
                }
                .addOnFailureListener { e ->
                    Log.e("Analytics", "Error registrando visita: ${e.message}")
                }

        }.addOnFailureListener { e ->
            Log.e("Analytics", "Error obteniendo documento de Firestore: ${e.message}")
        }
    }

}