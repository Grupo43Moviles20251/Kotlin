package com.moviles2025.freshlink43.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.moviles2025.freshlink43.data.repository.HomeRepository
import android.content.Context
import android.util.Log
import com.moviles2025.freshlink43.data.repository.SearchRepository
import com.moviles2025.freshlink43.ui.home.Restaurant

class SearchViewModel : ViewModel() {

    // Estado para la lista de restaurantes
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> get() = _restaurants

    // Estado para manejar errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val repository = SearchRepository()

    fun getFilteredRestaurants(query: String) {
        println("Llamando a getFilteredRestaurants con el query: $query")
        viewModelScope.launch {
            repository.getFilteredRestaurants(query) { restaurants, error ->
                if (restaurants != null) {
                    _restaurants.value = restaurants
                    println("Lista de restaurantes filtrada cargada correctamente: $restaurants")
                } else {
                    _errorMessage.value = error
                }
            }
        }
    }

    fun getFilteredRestaurantsByType(type: String) {
        println("Llamando a getFilteredRestaurantsByType con el tipo: $type")
        viewModelScope.launch {
            repository.getFilteredRestaurantsByType(type) { restaurants, error ->
                if (restaurants != null) {
                    _restaurants.value = restaurants
                    println("Lista de restaurantes filtrada por tipo cargada correctamente: $restaurants")
                } else {
                    _errorMessage.value = error
                }
            }
        }
    }
}