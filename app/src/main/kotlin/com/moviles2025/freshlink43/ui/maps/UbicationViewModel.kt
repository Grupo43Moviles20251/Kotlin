package com.moviles2025.freshlink43.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
//import com.moviles2025.freshlink43.data.repository.RestaurantRepository
import com.moviles2025.freshlink43.ui.home.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UbicationViewModel(
    //private val repository: RestaurantRepository,
    private val mapFacade: MapFacade
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> get() = _userLocation

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> get() = _restaurants

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
    }

    fun loadNearbyRestaurants() {
        userLocation.value?.let { location ->
            viewModelScope.launch {
                //_restaurants.value = repository.getNearbyRestaurants(location)
            }
        }
    }

    fun updateMapMarkers(googleMap: GoogleMap) {
        _restaurants.value.let { restaurants ->
            mapFacade.addRestaurantMarkers(googleMap, restaurants)
        }
    }
}