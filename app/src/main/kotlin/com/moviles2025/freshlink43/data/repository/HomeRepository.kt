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

class HomeRepository(
    private val backendServiceAdapter: BackendServiceAdapter,
    private val connectivityHandler: ConnectivityHandler,
    private val context: Context
) {

    private val connection: StateFlow<Boolean> = connectivityHandler.isConnected
    val isConnected = connection.value

    suspend fun getRestaurants(): Result<List<Restaurant>> {
        // Si hay conexión a internet, hacemos la solicitud al backend
        return if (isConnected) {
            val result = backendServiceAdapter.fetchRestaurants()

            return if (result.isSuccess) {
                val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
                val domainList = dtoList.map { it.toDomain() }

                // Borramos el caché y luego guardamos los primeros 5 restaurantes
                //clearCache(context)
                saveRestaurantsToCache(context,domainList)

                Result.success(domainList)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } else {
            // Si no hay conexión a internet, buscamos los restaurantes en el caché
            val cachedRestaurants = getRestaurantsFromCache(context)

            return if (cachedRestaurants.isNotEmpty()) {
                // Si hay restaurantes en caché, los devolvemos
                Result.success(cachedRestaurants)
            } else {
                // Si no hay datos en caché, devolvemos un error
                Result.failure(Exception("No internet connection and no cached data available"))
            }
        }
    }
}