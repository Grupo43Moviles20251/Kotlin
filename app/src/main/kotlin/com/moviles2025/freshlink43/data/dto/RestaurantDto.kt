package com.moviles2025.freshlink43.data.dto

import com.moviles2025.freshlink43.model.Product
import com.moviles2025.freshlink43.model.Restaurant

data class RestaurantDto(
    val name: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val rating: Double,
    val type: Int,
    val products: List<ProductDto> // ⬅️ Agregamos los productos
) {
    fun toDomain(): Restaurant {
        return Restaurant(
            name = name,
            imageUrl = imageUrl,
            description = description,
            latitude = latitude,
            longitude = longitude,
            address = address,
            products = products.map { it.toDomain() }, // ⬅️ Convertir productos también
            rating = rating,
            type = type
        )
    }
}