package com.moviles2025.freshlink43.data.repository

import android.util.Log
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import com.moviles2025.freshlink43.data.dto.RestaurantMaps
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val firebaseServiceAdapter: FirebaseServiceAdapter
) {
    suspend fun getAllRestaurants(): List<RestaurantMaps> {
        return try {
            val list = firebaseServiceAdapter.getRestaurantsFromFirestore()
            Log.d("RestaurantRepository", " Restaurantes obtenidos: ${list.size}")
            list
        } catch (e: Exception) {
            Log.e("RestaurantRepository", " Error al obtener restaurantes: ${e.message}")
            emptyList()
        }
    }
}