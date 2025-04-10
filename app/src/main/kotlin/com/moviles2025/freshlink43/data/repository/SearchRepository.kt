package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val backendServiceAdapter: BackendServiceAdapter
) {

    fun getFilteredRestaurants(query: String, callback: (List<Restaurant>?, String?) -> Unit) {
        backendServiceAdapter.fetchRestaurantsByQuery(query) { dtoList, error ->
            if (dtoList != null) {
                val restaurants = dtoList.map { it.toDomain() }
                callback(restaurants, null)
            } else {
                callback(null, error)
            }
        }
    }

    fun getAllRestaurants(callback: (List<Restaurant>?, String?) -> Unit) {
        backendServiceAdapter.fetchRestaurants { dtoList, error ->
            if (dtoList != null) {
                val restaurants = dtoList.map { it.toDomain() }
                callback(restaurants, null)
            } else {
                callback(null, error)
            }
        }
    }

    fun getFilteredRestaurantsByType(type: String, callback: (List<Restaurant>?, String?) -> Unit) {
        backendServiceAdapter.fetchRestaurantsByType(type) { dtoList, error ->
            if (dtoList != null) {
                val restaurants = dtoList.map { it.toDomain() }
                callback(restaurants, null)
            } else {
                callback(null, error)
            }
        }
    }
}