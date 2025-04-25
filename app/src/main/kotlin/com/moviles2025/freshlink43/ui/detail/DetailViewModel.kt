package com.moviles2025.freshlink43.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.DetailRepository
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.network.ConnectivityHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val connectivityHandler: ConnectivityHandler
) : ViewModel() {

    private val _restaurant = MutableStateFlow<Restaurant>(Restaurant())
    val restaurant: StateFlow<Restaurant> = _restaurant

    val isConnected: StateFlow<Boolean> = connectivityHandler.isConnected

    fun getRestaurantDetail(productId: Int) {
        viewModelScope.launch {
            val result = repository.getRestaurantDetail(productId)

            result
                .onSuccess { restaurant ->
                    println("Datos recibidos: $restaurant")
                    _restaurant.value = restaurant
                    println("Restaurante actualizado: ${_restaurant.value}")
                }
                .onFailure { error ->
                    println("Error obteniendo restaurante: ${error.localizedMessage}")
                    // Aquí puedes mostrar un mensaje de error si quieres
                }
        }
    }


}