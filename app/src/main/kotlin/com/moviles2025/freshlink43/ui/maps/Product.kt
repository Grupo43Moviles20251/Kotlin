package com.moviles2025.freshlink43.ui.maps

data class Product(
    val productId: Int = 0,
    val productName: String = "",
    val originalPrice: Int = 0,
    val discountPrice: Int = 0,
    val amount: Int = 0,
    val available: Boolean = false
)