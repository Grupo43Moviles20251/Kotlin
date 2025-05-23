package com.moviles2025.freshlink43.model



data class Restaurant(
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val products: List<Product> = emptyList(),
    val rating: Double = 0.0,
    val type: Int = 0
)



