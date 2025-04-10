package com.moviles2025.freshlink43.data.dto

import com.moviles2025.freshlink43.ui.maps.Product


data class RestaurantMaps(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val rating: Double = 0.0,
    val type: Int = 0,
    val products: List<Product> = emptyList()
)