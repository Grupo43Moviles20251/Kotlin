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
import com.moviles2025.freshlink43.data.services.RestaurantService
import com.moviles2025.freshlink43.model.RestaurantMaps
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@HiltViewModel
class UbicationViewModel  @Inject constructor(
    @ApplicationContext private val context: Context,

    //private val context: Context,
    private val repository: RestaurantService,
    val mapFacade: MapFacade
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _restaurants = MutableStateFlow<List<RestaurantMaps>>(emptyList())
    val restaurants: StateFlow<List<RestaurantMaps>> = _restaurants.asStateFlow()


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
                        Log.d("UbicationViewModel", "Ubicación obtenida: $latLng")
                        _userLocation.value = latLng
                        mapFacade.moveCameraToLocation(latLng)
                        loadNearbyRestaurants()
                    } ?: Log.e("UbicationViewModel", "No se pudo obtener la ubicación")
                }.addOnFailureListener {
                    Log.e("UbicationViewModel", "Error obteniendo ubicación: ${it.message}")
                }
            } catch (e: Exception) {
                Log.e("UbicationViewModel", "Excepción en requestUserLocation: ${e.message}")
            }
        }
    }
    private fun loadNearbyRestaurants() {
        viewModelScope.launch {
            val data = repository.getAllRestaurants()
            Log.d("UbicationViewModel", "📍 Restaurantes obtenidos: ${data.size}")
            _restaurants.value = data
        }
    }

    fun updateMapMarkers() {
        mapFacade.addRestaurantMarkers(_restaurants.value)
    }

    fun initializeMap(it: Any, userLocation: LatLng) {

    }
}