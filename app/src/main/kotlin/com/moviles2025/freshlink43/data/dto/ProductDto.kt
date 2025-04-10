package com.moviles2025.freshlink43.data.dto

import com.moviles2025.freshlink43.model.Product

data class ProductDto(
    val productId: Int,
    val productName: String,
    val amount: Int,
    val available: Boolean,
        val discountPrice: Double,
    val originalPrice: Double
) {
    fun toDomain(): Product {
        return Product(
            productId = productId,
            productName = productName,
            amount = amount,
            available = available,
            discountPrice = discountPrice,
            originalPrice = originalPrice
        )
    }
}