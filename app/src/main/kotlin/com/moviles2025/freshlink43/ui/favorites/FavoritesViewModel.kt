package com.moviles2025.freshlink43.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.FavoriteRepository
import com.moviles2025.freshlink43.data.repository.HomeRepository
import com.moviles2025.freshlink43.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Restaurant>>(emptyList())
    val favorites: StateFlow<List<Restaurant>> = _favorites

    private var allRestaurants = listOf<Restaurant>() // Aquí guardamos todos para filtrar

    init {
        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            homeRepository.getRestaurants { restaurants, error ->
                if (restaurants != null) {
                    allRestaurants = restaurants
                    updateFavorites()
                } else {
                    println("Error fetching favorites: $error")
                }
            }
        }
    }

    private fun updateFavorites() {
        _favorites.value = favoriteRepository.getFavorites(allRestaurants)
    }

    fun toggleFavorite(restaurant: Restaurant) {
        if (favoriteRepository.isFavorite(restaurant)) {
            favoriteRepository.removeFavorite(restaurant)
        } else {
            favoriteRepository.addFavorite(restaurant)
        }
        updateFavorites()
    }

    fun isFavorite(restaurant: Restaurant): Boolean {
        return favoriteRepository.isFavorite(restaurant)
    }
}