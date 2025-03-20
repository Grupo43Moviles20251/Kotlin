package com.moviles2025.freshlink43.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moviles2025.freshlink43.ui.home.Restaurant

@Composable
fun UbicationScreen(navController: NavController,
                    viewModel: UbicationViewModel) {
    val context = LocalContext.current
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            hasLocationPermission -> {
                userLocation?.let { location ->
                    MapViewComponent(viewModel, location, restaurants)
                } ?: run {
                    Text("Obteniendo ubicación...", modifier = Modifier.align(Alignment.Center))
                }
            }
            else -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Se necesitan permisos de ubicación para mostrar restaurantes cercanos.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        requestPermissionsLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }) {
                        Text("Solicitar permisos")
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapViewComponent(viewModel: UbicationViewModel, userLocation: LatLng, restaurants: List<Restaurant>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        Marker(
            state = MarkerState(position = userLocation),
            title = "Tu ubicación",
            snippet = "Aquí estás"
        )

        restaurants.forEach { restaurant ->
            Marker(
                state = MarkerState(position = LatLng(restaurant.latitude, restaurant.longitude)),
                title = restaurant.name,
                snippet = "Descuento: ${restaurant.products[0].discountPrice}$"
            )
        }
    }
}