package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.cacheHandler.getOrdersFromCache
import com.moviles2025.freshlink43.cacheHandler.saveOrdersToCache
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Order
import com.moviles2025.freshlink43.network.ConnectivityHandler
import kotlinx.coroutines.flow.StateFlow
import android.content.Context

class OrderRepository(
    private val backendServiceAdapter: BackendServiceAdapter,
    private val connectivityHandler: ConnectivityHandler,
    private val context: Context
) {
    private val connection: StateFlow<Boolean> = connectivityHandler.isConnected
    val isConnected = connection.value

    suspend fun getOrders(): Result<List<Order>> {
        // Si hay conexión a internet, hacemos la solicitud al backend
        return if (isConnected) {
            val result = backendServiceAdapter.getOrders()

            return if (result.isSuccess) {
                val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
                val domainList = dtoList.map { it.toDomain() }

                // Borramos el caché y luego guardamos los primeros 5 restaurantes
                saveOrdersToCache(context, domainList)

                Result.success(domainList)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } else {
            // Si no hay conexión a internet, buscamos los restaurantes en el caché
            val cachedOrders = getOrdersFromCache(context)

            return if (cachedOrders.isNotEmpty()) {
                // Si hay restaurantes en caché, los devolvemos
                Result.success(cachedOrders)
            } else {
                // Si no hay datos en caché, devolvemos un error
                Result.failure(Exception("No internet connection and no cached data available"))
            }
        }
    }

    suspend fun cancelOrder(orderId: String): Result<Unit> {
        return if (isConnected) {
            backendServiceAdapter.cancelOrder(orderId)
        } else {
            Result.failure(Exception("No internet connection"))
        }
    }
}
