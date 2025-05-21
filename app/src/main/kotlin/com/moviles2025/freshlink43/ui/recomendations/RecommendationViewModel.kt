package com.moviles2025.freshlink43.ui.recomendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.FavoriteRepository
import com.moviles2025.freshlink43.data.repository.HomeRepository
import com.moviles2025.freshlink43.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import com.moviles2025.freshlink43.network.ConnectivityHandler



@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val connectivityHandler: ConnectivityHandler
) : ViewModel() {

    private val _welcomeMessage = MutableStateFlow("Welcome to FreshLink!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val isConnected: StateFlow<Boolean> = connectivityHandler.isConnected


    fun getRestaurants() {
        viewModelScope.launch {
            val result = repository.getRestaurants()

            result
                .onSuccess { restaurantList ->
                    _restaurants.value = restaurantList
                }
                .onFailure { error ->
                    _errorMessage.value = error.localizedMessage ?: "Error al cargar restaurantes"
                }
        }
    }

    fun toggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            if (favoriteRepository.isFavorite(restaurant)) {
                favoriteRepository.removeFavorite(restaurant)
            } else {
                favoriteRepository.addFavorite(restaurant)
            }
            //updateFavorites()
        }
    }

    fun isFavorite(restaurant: Restaurant, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = favoriteRepository.isFavorite(restaurant)
            callback(result)
        }
    }
}
