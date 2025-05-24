package com.moviles2025.freshlink43.ui.home

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
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val connectivityHandler: ConnectivityHandler
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 5
    }

    private val _allRestaurants     = MutableStateFlow<List<Restaurant>>(emptyList())
    private val _visibleRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())


    val visibleRestaurants: StateFlow<List<Restaurant>> = _visibleRestaurants
    val welcomeMessage: StateFlow<String>get() = _welcomeMessage
    val errorMessage: StateFlow<String?>get() = _errorMessage
    val isConnected: StateFlow<Boolean>get() = connectivityHandler.isConnected


    private val _welcomeMessage = MutableStateFlow("Welcome to FreshLink!")
    private val _errorMessage   = MutableStateFlow<String?>(null)


    private var currentPage    = 0
    private var isLoadingPage  = false

    fun getRestaurants() {
        viewModelScope.launch {
            repository.getRestaurants()
                .onSuccess { list ->
                    //Guarda TODAS las órdenes
                    _allRestaurants.value = list
                    //Reinicia la paginación
                    currentPage = 0
                    _visibleRestaurants.value = emptyList()
                    //Carga la primera página
                    loadNextPage()
                }
                .onFailure { err ->
                    _errorMessage.value = err.localizedMessage ?: "Error al cargar restaurantes"
                }
        }
    }

    fun loadNextPage() {
        if (isLoadingPage) return

        val all = _allRestaurants.value
        val fromIndex = currentPage * PAGE_SIZE
        if (fromIndex >= all.size) return  // no hay más

        isLoadingPage = true
        val toIndex = (fromIndex + PAGE_SIZE).coerceAtMost(all.size)

        // sublista 0 until toIndex → página + páginas anteriores
        _visibleRestaurants.value = all.subList(0, toIndex)

        currentPage++
        isLoadingPage = false
    }

    fun hasMorePages(): Boolean =
        _visibleRestaurants.value.size < _allRestaurants.value.size

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
