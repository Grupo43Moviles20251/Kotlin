package com.moviles2025.freshlink43.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.moviles2025.freshlink43.data.repository.HomeRepository
import android.content.Context
import android.util.Log


class HomeViewModel : ViewModel() {

    private val _welcomeMessage = MutableStateFlow("Welcome to FreshLink!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    // Estado para la lista de restaurantes
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> get() = _restaurants

    // Estado para manejar errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val repository = HomeRepository()


    // Función para obtener los restaurantes
    fun getRestaurants() {
        println("Llamando a getRestaurants")
        viewModelScope.launch {
            repository.getRestaurants { restaurants, error ->
                if (restaurants != null) {
                    _restaurants.value = restaurants
                    println("Lista de restaurantes cargada correctamente: $restaurants")

                } else {
                    _errorMessage.value = error
                }
            }
        }
    }
}