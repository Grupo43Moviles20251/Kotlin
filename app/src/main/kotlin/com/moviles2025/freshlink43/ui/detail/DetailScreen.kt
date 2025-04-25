package com.moviles2025.freshlink43.ui.detail

import android.icu.text.DecimalFormat
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextDecoration
import com.moviles2025.freshlink43.utils.corporationGreen
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.google.maps.android.compose.MapEffect
import com.moviles2025.freshlink43.ui.maps.UbicationViewModel
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.moviles2025.freshlink43.utils.NotConnection


@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel,
    ubicationViewModel: UbicationViewModel,
    productId: Int
) {
    val restaurant by viewModel.restaurant.collectAsStateWithLifecycle()
    viewModel.getRestaurantDetail(productId)

    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "detail"

    val isConnected = viewModel.isConnected.collectAsState(initial = false).value

    Scaffold(
        topBar = { Header { navController.navigate("profile") } },
        bottomBar = { BottomNavManager(navController, currentRoute) },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Imagen del restaurante con opacidad
            val painter = rememberImagePainter(
                data = restaurant.imageUrl,
                builder = { crossfade(true) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Restaurant Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

            }

            Text(
                text = restaurant.name,
                fontSize = 40.sp,
                color = corporationGreen,
                fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título del producto
            Text(
                text = restaurant.products.getOrNull(0)?.productName ?: "No product available",
                fontSize = 25.sp,
                color = corporationGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
            )

            // Descripción del producto
            Text(
                text = restaurant.description,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 15.dp)
            )

            // Fila de botones y precios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Implementar acción de orden */ },
                    modifier = Modifier
                        .widthIn(min = 0.dp, max = LocalConfiguration.current.screenWidthDp.dp / 3) // Ocupa un tercio del ancho de la pantalla
                        .height(48.dp), // Ajusta la altura si es necesario
                    colors = ButtonDefaults.buttonColors(
                        containerColor = corporationGreen, // Color de fondo
                        contentColor = Color.White  // Color del texto
                    )
                ) {
                    Text(
                        text = "Order",
                        fontSize = 20.sp,
                    )
                }

                // Precios a la derecha en una Row
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val discount = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0
                    val original = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0

                    Text(
                        text = "$${formatAmount(discount)}",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "$${formatAmount(original)}",
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                        color = corporationGreen
                    )
                }
            }

            // Mapa con la ubicación del restaurante
            val restaurantLocation = LatLng(restaurant.latitude, restaurant.longitude)
            val cameraPositionState = rememberCameraPositionState {
                // Inicializamos la cámara con la ubicación del restaurante y un zoom adecuado
                position = CameraPosition.fromLatLngZoom(restaurantLocation, 15f)
            }
            if(isConnected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true),
                        uiSettings = MapUiSettings(zoomControlsEnabled = true)
                    ) {
                        MapEffect(ubicationViewModel) { map ->
                            ubicationViewModel.initializeMap(map, restaurantLocation)
                        }

                        // Mover la cámara para centrarla en la ubicación del restaurante
                        LaunchedEffect(restaurantLocation) {
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15f))
                        }

                        // Marcador de la ubicación del restaurante
                        Marker(
                            state = MarkerState(position = restaurantLocation),
                            title = restaurant.name,
                            snippet = restaurant.address
                        )
                    }
                }
            } else{
                NotConnection()
            }
        }
    }
}


fun formatAmount(amount: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}



