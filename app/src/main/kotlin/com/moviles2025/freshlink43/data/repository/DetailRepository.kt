package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant

class DetailRepository(
    private val backendServiceAdapter: BackendServiceAdapter
) {
    suspend fun getRestaurantDetail(productId: Int): Result<Restaurant> {
        val result = backendServiceAdapter.fetchRestaurantDetails(productId)

        return if (result.isSuccess) {
            val dto = result.getOrNull() ?: return Result.failure(Exception("Null body"))
            Result.success(dto.toDomain())
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}