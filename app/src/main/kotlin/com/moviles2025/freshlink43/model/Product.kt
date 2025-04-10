package com.moviles2025.freshlink43.model

import androidx.annotation.Keep

@Keep
data class Product(
    val productId: Int = 0,
    val productName: String = "",
    val amount: Int = 0,
    val available: Boolean = false,
    val discountPrice: Double = 0.0,
    val originalPrice: Double = 0.0
)