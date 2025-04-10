package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant

class HomeRepository(
    private val backendServiceAdapter: BackendServiceAdapter
) {

    fun getRestaurants(callback: (List<Restaurant>?, String?) -> Unit) {
        backendServiceAdapter.fetchRestaurants { dtoList, error ->
            if (dtoList != null) {
                val restaurants = dtoList.map { it.toDomain() }
                callback(restaurants, null)
            } else {
                callback(null, error)
            }
        }
    }
}