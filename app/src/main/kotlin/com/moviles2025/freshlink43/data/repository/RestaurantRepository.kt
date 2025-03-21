package com.moviles2025.freshlink43.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.moviles2025.freshlink43.ui.maps.Restaurant
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getAllRestaurants(): List<Restaurant> {
        return try {
            val snapshot = firestore.collection("retaurants").get().await()  // asegúrate de que esto esté bien escrito
            val list = snapshot.documents.mapNotNull { it.toObject(Restaurant::class.java) }
            Log.d("RestaurantRepository", " Restaurantes obtenidos: ${list.size}")
            list
        } catch (e: Exception) {
            Log.e("RestaurantRepository", " Error al obtener restaurantes: ${e.message}")
            emptyList()
        }
    }
}