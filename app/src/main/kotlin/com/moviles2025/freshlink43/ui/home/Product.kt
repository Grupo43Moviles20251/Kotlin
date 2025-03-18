package com.moviles2025.freshlink43.ui.home

data class Product(
    val productId: Int,
    val productName: String,
    val amount: Int,
    val available: Boolean,
    val discountPrice: Double,
    val originalPrice: Double
)