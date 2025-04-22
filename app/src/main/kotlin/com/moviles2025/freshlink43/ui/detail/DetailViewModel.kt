package com.moviles2025.freshlink43.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.DetailRepository
import com.moviles2025.freshlink43.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository
) : ViewModel() {

    private val _restaurant = MutableStateFlow<Restaurant>(Restaurant())
    val restaurant: StateFlow<Restaurant> = _restaurant

    fun getRestaurantDetail(productId:Int) {
        viewModelScope.launch {
            repository.getRestaurantDetail(productId) { restaurant, error ->
                if (restaurant != null) {
                    _restaurant.value = restaurant
                }
            }
        }
    }

}