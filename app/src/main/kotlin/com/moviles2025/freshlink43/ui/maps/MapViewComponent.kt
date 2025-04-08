package com.moviles2025.freshlink43.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.model.RestaurantMaps
import com.moviles2025.freshlink43.utils.corporationBlue

@SuppressLint("MissingPermission")
@Composable
fun MapViewComponent(
    viewModel: UbicationViewModel,
    userLocation: LatLng,
    restaurantMaps: List<RestaurantMaps>
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    var selectedRestaurant by remember { mutableStateOf<RestaurantMaps?>(null) }

    LaunchedEffect(restaurantMaps) {
        viewModel.updateMapMarkers()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            MapEffect(viewModel) { map ->
                viewModel.initializeMap(map, userLocation)
            }


            restaurantMaps.forEach { restaurant ->
                Marker(
                    state = MarkerState(position = LatLng(restaurant.latitude, restaurant.longitude)),
                    title = restaurant.name,
                    snippet = restaurant.address,
                    onClick = {
                        selectedRestaurant = restaurant
                        false
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(userLocation, 15f)) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            containerColor = corporationBlue
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Centrar mapa en tu ubicación")
        }

        AnimatedVisibility(
            visible = selectedRestaurant != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedRestaurant?.let { restaurant ->
                RestaurantCard(
                    restaurantMaps = restaurant,
                    onClose = { selectedRestaurant = null },
                    onNavigate = {
                        val gmmIntentUri =
                            Uri.parse("google.navigation:q=${restaurant.latitude},${restaurant.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(mapIntent)
                    }
                )
            }
        }

        // Botón para centrar en la ubicación del usuario

    }
}

@Composable
fun RestaurantCard(
    restaurantMaps: RestaurantMaps,
    onClose: () -> Unit,
    onNavigate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(restaurantMaps.imageUrl),
                contentDescription = restaurantMaps.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = restaurantMaps.name,
                style = MaterialTheme.typography.headlineMedium,
                color = corporationBlue,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )
            Text(
                text = restaurantMaps.address,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )
            Text(
                text = restaurantMaps.description,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                Text(
                    text = restaurantMaps.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Common ticket: $${restaurantMaps.products.firstOrNull()?.discountPrice ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onNavigate) {
                    Text("How to go",
                        fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
                }
                OutlinedButton(onClick = onClose) {
                    Text("Close",
                        fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
                }
            }
        }
    }
}