package com.moviles2025.freshlink43.data.repository

import android.content.Context
import com.moviles2025.freshlink43.cacheHandler.getRestaurantsFromCache
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.network.ConnectivityHandler
import kotlinx.coroutines.flow.StateFlow

class DetailRepository(
    private val backendServiceAdapter: BackendServiceAdapter,
    private val connectivityHandler: ConnectivityHandler,
    private val context: Context
) {

    //private val connection: StateFlow<Boolean> = connectivityHandler.isConnected
    //val isConnected = connection.value

    suspend fun getRestaurantDetail(productId: Int): Result<Restaurant> {
        val isConnected = connectivityHandler.hasInternetConnection()
        return if(!isConnected){
            // Si no hay conexión a internet, buscamos los restaurantes en el caché
            val cachedRestaurants = getRestaurantsFromCache(context)

            return if (cachedRestaurants.isNotEmpty()) {
                // Si hay restaurantes en caché, los devolvemos
                Result.success(searchRestaurantById(productId, cachedRestaurants))
            } else {
                // Si no hay datos en caché, devolvemos un error
                Result.failure(Exception("No internet connection and no cached data available"))
            }
        } else{
            val result = backendServiceAdapter.fetchRestaurantDetails(productId)

            return if (result.isSuccess) {
                val dto = result.getOrNull() ?: return Result.failure(Exception("Null body"))
                Result.success(dto.toDomain())
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    private fun searchRestaurantById(id: Int, restaurants: List<Restaurant>): Restaurant {
        for (restaurant in restaurants) {
            if (restaurant.products[0].productId == id) {
                return restaurant
            }
        }
        return Restaurant() // Devuelve un restaurante vacío si no se encuentra el id
    }

    suspend fun getOrderCode(restaurantId: String, product:String, price:String): Result<String> {

        val result = backendServiceAdapter.fetchOrder(restaurantId, product, price)

        return if(!result.isSuccess){
            Result.failure(result.exceptionOrNull() ?: Exception("Error en el Repository papu"))
        } else{
            return result
        }
    }
}