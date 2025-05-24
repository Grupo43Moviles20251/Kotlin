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

    suspend fun getOrders(): Result<List<Order>> {
        // Si hay conexión a internet, hacemos la solicitud al backend
        val isConnected = connectivityHandler.hasInternetConnection()

        return if (isConnected) {
            val result = backendServiceAdapter.getOrders()

            if (result.isSuccess) {
                val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
                val domainList = dtoList.map { it.toDomain() }

                // Guardamos las primeras 4 órdenes en caché
                saveOrdersToCache(context, domainList)

                Result.success(domainList)
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } else {
            // Si no hay conexión a internet, buscamos las órdenes en el caché
            val cachedOrders = getOrdersFromCache(context)

            if (cachedOrders.isNotEmpty()) {
                // Si hay órdenes en caché, las devolvemos
                return Result.success(cachedOrders)
            } else {
                // Si no hay datos en caché, devolvemos un error
                return Result.failure(Exception("No internet connection and no cached data available"))
            }
        }
    }

    suspend fun cancelOrder(orderId: String): Result<Unit> {
        val isConnected = connectivityHandler.hasInternetConnection()
        return if (isConnected) {
            backendServiceAdapter.cancelOrder(orderId)
        } else {
            Result.failure(Exception("No internet connection"))
        }
    }
}
