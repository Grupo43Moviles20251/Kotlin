package com.moviles2025.freshlink43.model

import com.moviles2025.freshlink43.entities.RestaurantEntity


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

// Función de extensión para convertir Restaurant a RestaurantEntity
fun Restaurant.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        rating = this.rating,
        type = this.type,
        products = this.products.map { it.toEntity() }  // Convierte cada Product a ProductEntity
    )
}


