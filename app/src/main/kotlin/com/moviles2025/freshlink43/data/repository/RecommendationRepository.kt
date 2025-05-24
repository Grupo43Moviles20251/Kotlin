package com.moviles2025.freshlink43.data.repository

import androidx.compose.runtime.collectAsState
import com.moviles2025.freshlink43.cacheHandler.getRestaurantsFromCache
import com.moviles2025.freshlink43.cacheHandler.saveRestaurantsToCache
import com.moviles2025.freshlink43.cacheHandler.clearCache
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.network.ConnectivityHandler
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import com.moviles2025.freshlink43.data.dto.RestaurantMaps
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RecommendationRepository (
    private val firebaseServiceAdapter: FirebaseServiceAdapter,
    private val connectivityHandler: ConnectivityHandler,
    private val context: Context
) {

    private val connection: StateFlow<Boolean> = connectivityHandler.isConnected
    val isConnected = connection.value



    suspend fun getRestaurants(): Result<List<Restaurant>> {
        return if (isConnected) {
            try {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val monthYear = dateFormat.format(calendar.time)

                val topNamesResult = firebaseServiceAdapter.getTopRestaurantsFromVisitsThisMonth(monthYear)
                if (topNamesResult.isFailure) return Result.failure(topNamesResult.exceptionOrNull()!!)

                val topNames = topNamesResult.getOrNull() ?: emptyList()
                if (topNames.isEmpty()) return Result.failure(Exception("No top restaurants found"))

                val restaurantsResult = firebaseServiceAdapter.getRestaurantsByNames(topNames)
                if (restaurantsResult.isFailure) return Result.failure(restaurantsResult.exceptionOrNull()!!)

                val restaurants = restaurantsResult.getOrNull() ?: emptyList()

                // Ordenar lista según orden de topNames para mantener ranking descendente
                val restaurantsOrdered = topNames.mapNotNull { name ->
                    restaurants.find { it.name == name }
                }

                saveRestaurantsToCache(context, restaurantsOrdered)

                Result.success(restaurantsOrdered)

            } catch (e: Exception) {
                val cachedRestaurants = getRestaurantsFromCache(context)
                if (cachedRestaurants.isNotEmpty()) {
                    Result.success(cachedRestaurants)
                } else {
                    Result.failure(e)
                }
            }
        } else {
            val cachedRestaurants = getRestaurantsFromCache(context)
            if (cachedRestaurants.isNotEmpty()) {
                Result.success(cachedRestaurants)
            } else {
                Result.failure(Exception("No internet connection and no cached data available"))
            }
        }
    }

}
