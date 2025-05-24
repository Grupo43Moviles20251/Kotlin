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

    companion object {
        private const val PAGE_SIZE = 10
    }

    // 1) Flujos internos
    private val _allOrders = MutableStateFlow<List<Order>>(emptyList())
    private val _visibleOrders = MutableStateFlow<List<Order>>(emptyList())
    val isConnected: StateFlow<Boolean> = connectivityHandler.isConnected

    // 2) Exposición al UI
    val visibleOrders: StateFlow<List<Order>> = _visibleOrders

    // 3) Variables de control
    private var currentPage = 0
    private var isLoadingPage = false

    fun getOrders() {
        viewModelScope.launch {
            repository.getOrders()
                .onSuccess { list ->
                    _allOrders.value = list
                    currentPage = 0
                    _visibleOrders.value = emptyList()
                    loadNextPage()
                }
                .onFailure { err ->
                    // … tu lógica de error
                }
        }
    }

    fun cancelOrder(orderId: String) {
        // 1) Guarda el estado previo para poder revertir
        val prevAll     = _allOrders.value
        val prevVisible = _visibleOrders.value

        // 2) Aplica cambio optimista: marca como "cancelled"
        _allOrders.value     = prevAll.map { if (it.orderId == orderId) it.copy(state = "cancelled") else it }
        _visibleOrders.value = prevVisible.map { if (it.orderId == orderId) it.copy(state = "cancelled") else it }

        // 3) Lanza la petición al backend
        viewModelScope.launch {
            val result = repository.cancelOrder(orderId)

            if (result.isFailure) {
                // 4) En caso de error, revierte ambos flujos y emite mensaje
                _allOrders.value     = prevAll
                _visibleOrders.value = prevVisible
            }
        }
    }

    fun loadNextPage() {
        if (isLoadingPage) return
        val all = _allOrders.value
        val fromIndex = currentPage * PAGE_SIZE
        if (fromIndex >= all.size) return  // ya no hay más
        isLoadingPage = true

        // Calcula el toIndex seguro
        val toIndex = (fromIndex + PAGE_SIZE).coerceAtMost(all.size)
        // Toma la “sublista” desde 0 hasta toIndex
        _visibleOrders.value = all.subList(0, toIndex)

        currentPage++
        isLoadingPage = false
    }

    fun hasMorePages(): Boolean {
        return _visibleOrders.value.size < _allOrders.value.size
    }

}