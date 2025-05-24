package com.moviles2025.freshlink43.model

data class Order(
    val orderId: String = "",
    val productName: String = "",
    val price: String = "",
    val date : String = "",
    var state : String = ""
)
