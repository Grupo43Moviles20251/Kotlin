package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val backendServiceAdapter: BackendServiceAdapter
) {

    suspend fun getFilteredRestaurants(query: String): Result<List<Restaurant>> {
        val result = backendServiceAdapter.fetchRestaurantsByQuery(query)
        return if (result.isSuccess) {
            val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
            Result.success(dtoList.map { it.toDomain() })
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getAllRestaurants(): Result<List<Restaurant>> {
        val result = backendServiceAdapter.fetchRestaurants()
        return if (result.isSuccess) {
            val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
            Result.success(dtoList.map { it.toDomain() })
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getFilteredRestaurantsByType(type: String): Result<List<Restaurant>> {
        val result = backendServiceAdapter.fetchRestaurantsByType(type)
        return if (result.isSuccess) {
            val dtoList = result.getOrNull() ?: return Result.failure(Exception("Empty result"))
            Result.success(dtoList.map { it.toDomain() })
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}