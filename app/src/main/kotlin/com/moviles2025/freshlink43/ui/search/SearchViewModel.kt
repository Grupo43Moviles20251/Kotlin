package com.moviles2025.freshlink43.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.SearchRepository
import com.moviles2025.freshlink43.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> get() = _restaurants

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        getFilteredRestaurants("")
    }

    fun getFilteredRestaurants(query: String) {
        println("Llamando a getFilteredRestaurants con el query: $query")
        viewModelScope.launch {
            _isLoading.value = true
            repository.getFilteredRestaurants(query) { restaurants, error ->
                _isLoading.value = false
                if (restaurants != null) {
                    _restaurants.value = restaurants
                } else {
                    _errorMessage.value = error
                }
            }
        }
    }

    fun getAllRestaurants() {
        println("Llamando a getRestaurants")
        viewModelScope.launch {
            repository.getAllRestaurants { restaurants, error ->
                if (restaurants != null) {
                    _restaurants.value = restaurants
                    println("Lista de restaurantes cargada correctamente: $restaurants")
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