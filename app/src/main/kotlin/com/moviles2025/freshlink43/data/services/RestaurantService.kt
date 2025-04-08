package com.moviles2025.freshlink43.data.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.model.RestaurantMaps
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getAllRestaurants(): List<RestaurantMaps> {
        return try {
            val snapshot = firestore.collection("retaurants").get().await()  // asegúrate de que esto esté bien escrito
            val list = snapshot.documents.mapNotNull { it.toObject(RestaurantMaps::class.java) }
            Log.d("RestaurantRepository", " Restaurantes obtenidos: ${list.size}")
            list
        } catch (e: Exception) {
            Log.e("RestaurantRepository", " Error al obtener restaurantes: ${e.message}")
            emptyList()
        }
    }
}