package com.moviles2025.freshlink43.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.services.HomeService
import com.moviles2025.freshlink43.model.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableStateFlow<List<Restaurant>>(emptyList())
    val favorites: StateFlow<List<Restaurant>> = _favorites

    private val homeService = HomeService()

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            homeService.getRestaurants { restaurants, error ->
                if (restaurants != null) {
                    // Aquí debes definir tu lógica real de qué es "favorito"
                    val favoriteRestaurants = restaurants.filter { restaurant ->
                        // Por ahora para simular, hacemos que sean favoritos por ejemplo
                        restaurant.name.contains("FreshBite") || restaurant.name.contains("McDonalds")
                    }
                    _favorites.value = favoriteRestaurants
                } else {
                    println("Error fetching favorites: $error")
                }
            }
        }
    }
}