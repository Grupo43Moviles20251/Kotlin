package com.moviles2025.freshlink43.ui.home


data class Restaurant(
    val name: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val products: List<Product>,
    val rating: Double,
    val type: Int
)