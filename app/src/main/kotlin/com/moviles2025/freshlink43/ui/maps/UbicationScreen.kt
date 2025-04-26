package com.moviles2025.freshlink43.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.NotConnection
import com.moviles2025.freshlink43.utils.corporationBlue

@Composable
fun UbicationScreen(navController: NavController, viewModel: UbicationViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("UbicationScreen")
    }

    val isConnected = viewModel.isConnected.collectAsState(initial = false).value

    val context = LocalContext.current
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // Solicita permisos solo si aún no han sido concedidos
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

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "Ubication"

    Scaffold(
        topBar = { Header { navController.navigate("profile") } },
        bottomBar = { BottomNavManager(navController, currentRoute) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if(isConnected){
                when {
                    hasLocationPermission -> {
                        userLocation?.let { location ->
                            MapViewComponent(viewModel, location, restaurants)
                        } ?: Text("Obtaining location...", modifier = Modifier.align(Alignment.Center))
                    }
                    else -> {
                        PermissionRequestView {
                            requestPermissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    }
                }
            } else {
                NotConnection()
            }
        }
    }
}

@Composable
fun PermissionRequestView(onRequestPermissions: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("We need your location to show you nearby restaurants")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRequestPermissions) {
            Text("Grant permission")
        }
    }
}