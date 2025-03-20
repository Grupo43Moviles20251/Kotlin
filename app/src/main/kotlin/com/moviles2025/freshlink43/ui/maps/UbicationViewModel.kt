package com.moviles2025.freshlink43.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.moviles2025.freshlink43.ui.home.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UbicationViewModel  @Inject constructor(
    // private val repository: RestaurantRepository,  // TODO: Conectar con el repositorio cuando esté listo
    val mapFacade: MapFacade
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    init {
        loadNearbyRestaurants()
    }

    fun updateUserLocation(location: LatLng) {
        _userLocation.value = location
        mapFacade.moveCameraToLocation(location)  // Mueve la cámara cuando cambia la ubicación
        loadNearbyRestaurants()  // Recarga los restaurantes cercanos
    }

    private fun loadNearbyRestaurants() {
        userLocation.value?.let { location ->
            viewModelScope.launch {
                // Simulación de carga de datos mientras el repositorio no está implementado
                // _restaurants.value = repository.getNearbyRestaurants(location)  // Descomentar cuando el repo esté listo

                // TODO: Simulación de datos para pruebas SIN productos
                _restaurants.value = listOf(
                    Restaurant(
                        name = "Pizza Express",
                        imageUrl = "https://example.com/pizza.jpg",
                        description = "Deliciosa pizza italiana",
                        latitude = location.latitude + 0.002,
                        longitude = location.longitude + 0.002,
                        address = "Calle 123",
                        rating = 4.5,
                        type = 2,
                        products = emptyList() // 🔹 Se deja vacío para no mostrar productos
                    ),
                    Restaurant(
                        name = "Burger King",
                        imageUrl = "https://example.com/burger.jpg",
                        description = "Hamburguesas a la parrilla",
                        latitude = location.latitude - 0.002,
                        longitude = location.longitude - 0.002,
                        address = "Avenida 456",
                        rating = 4.2,
                        type = 2,
                        products = emptyList() // 🔹 Se deja vacío para no mostrar productos
                    )
                )
            }
        }
    }

    fun updateMapMarkers() {
        mapFacade.addRestaurantMarkers(_restaurants.value)
    }

    fun initializeMap(it: Any, userLocation: LatLng) {

    }
}