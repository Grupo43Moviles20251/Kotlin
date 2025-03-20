package com.moviles2025.freshlink43.ui.maps

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.moviles2025.freshlink43.ui.home.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@HiltViewModel
class UbicationViewModel  @Inject constructor(
    @ApplicationContext private val context: Context,
    // private val repository: RestaurantRepository,  // TODO: Conectar con el repositorio cuando esté listo
    //private val context: Context,
    val mapFacade: MapFacade
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()


    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    init {
        loadNearbyRestaurants()
        requestUserLocation()
    }

    fun updateUserLocation(location: LatLng) {
        Log.d("UbicationViewModel", "📍 Nueva ubicación recibida: $location")  // 🔹 Verificar si se obtiene la ubicación
        _userLocation.value = location
        mapFacade.moveCameraToLocation(location)  // Mueve la cámara cuando cambia la ubicación
        loadNearbyRestaurants()  // Recarga los restaurantes cercanos
    }

    @SuppressLint("MissingPermission")
    fun requestUserLocation() {
        viewModelScope.launch {
            try {
                val cancellationTokenSource = CancellationTokenSource()
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        Log.d("UbicationViewModel", "📍 Ubicación obtenida: $latLng")
                        _userLocation.value = latLng
                        mapFacade.moveCameraToLocation(latLng)
                        loadNearbyRestaurants()
                    } ?: Log.e("UbicationViewModel", "⚠️ No se pudo obtener la ubicación")
                }.addOnFailureListener {
                    Log.e("UbicationViewModel", "❌ Error obteniendo ubicación: ${it.message}")
                }
            } catch (e: Exception) {
                Log.e("UbicationViewModel", "❌ Excepción en requestUserLocation: ${e.message}")
            }
        }
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
                    ),
                    Restaurant(
                        name = "One burrito",
                        imageUrl = "https://example.com/burger.jpg",
                        description = "burritos",
                        latitude = location.latitude - 0.004,
                        longitude = location.longitude - 0.004,
                        address = "cerca al ML",
                        rating = 4.0,
                        type = 2,
                        products = emptyList() // 🔹 Se deja vacío para no mostrar productos
                    ),
                    Restaurant(
                        name = "Wok",
                        imageUrl = "https://example.com/burger.jpg",
                        description = "Comida oriental",
                        latitude = location.latitude + 0.004,
                        longitude = location.longitude - 0.004,
                        address = "City U",
                        rating = 4.0,
                        type = 2,
                        products = emptyList() // 🔹 Se deja vacío para no mostrar productos
                    ),

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