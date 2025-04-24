package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant

class HomeRepository(
    private val backendServiceAdapter: BackendServiceAdapter
) {

    suspend fun getRestaurants(): Result<List<Restaurant>> {
        val result = backendServiceAdapter.fetchRestaurants()

        return if (result.isSuccess) {
            val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
            Result.success(dtoList.map { it.toDomain() })
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}