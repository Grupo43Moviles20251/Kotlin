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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    init {
        getFilteredRestaurants("") // 🔥 Carga todos los restaurantes al abrir la pantalla
    }
    fun getFilteredRestaurants(query: String) {
        println("Llamando a getFilteredRestaurants con el query: $query")
        viewModelScope.launch {
            _isLoading.value = true // Empieza a cargar
            repository.getFilteredRestaurants(query) { restaurants, error ->
                _isLoading.value = false // Termina de cargar
                if (restaurants != null) {
                    _restaurants.value = restaurants
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