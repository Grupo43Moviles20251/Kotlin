package com.moviles2025.freshlink43.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.OrderRepository
import com.moviles2025.freshlink43.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.network.ConnectivityHandler

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository,
    private val connectivityHandler: ConnectivityHandler
) : ViewModel(){

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val isConnected: StateFlow<Boolean> = connectivityHandler.isConnected

    fun getOrders() {
        viewModelScope.launch {
            val result = repository.getOrders()
            println("getOrders result: $result")

            result
                .onSuccess { orderList ->
                    _orders.value = orderList
                }
                .onFailure { error ->
                    _errorMessage.value = error.localizedMessage ?: "Error al cargar ordenes"
                }
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            val result = repository.cancelOrder(orderId)
            println("cancelOrder result: $result")

        }
    }

}