package com.moviles2025.freshlink43.ui.maps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.moviles2025.freshlink43.ui.utils.corporationBlue

@SuppressLint("MissingPermission")
@Composable
fun MapViewComponent(viewModel: UbicationViewModel, userLocation: LatLng, restaurants: List<com.moviles2025.freshlink43.ui.home.Restaurant>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            // 🔹 Inicializar el mapa correctamente con MapEffect
            MapEffect(viewModel) { map ->
                viewModel.initializeMap(map, userLocation) // Ahora sí pasa el GoogleMap al ViewModel
            }

            // Marcador de ubicación del usuario
            Marker(
                state = MarkerState(position = userLocation),
                title = "Your Location",
                snippet = "You are here",
            )


            restaurants.forEach { restaurant ->
                Marker(
                    state = MarkerState(position = LatLng(restaurant.latitude, restaurant.longitude)),
                    title = restaurant.name,
                    snippet = "Discount: ${restaurant.products.getOrNull(0)?.discountPrice ?: "N/A"}$"
                )
            }
        }

        FloatingActionButton(
            onClick = { cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(userLocation, 15f)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = corporationBlue
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Center map on user location")
        }
    }
}