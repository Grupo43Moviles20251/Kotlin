package com.moviles2025.freshlink43.data.dto

import com.moviles2025.freshlink43.model.Order

data class OrderDto(
    val orderId: String,
    val productName: String,
    val price: String,
    val date: String,
    val state: String
){
    fun toDomain(): Order {
        return Order(
            orderId = orderId,
            productName = productName,
            price = price,
            date = date,
            state = state
        )
    }
}
