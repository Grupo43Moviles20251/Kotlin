package com.moviles2025.freshlink43.ui.detail

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import coil.size.Size
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.ui.detail.DetailViewModel
import com.moviles2025.freshlink43.ui.home.PlaceholderRestaurantCard
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.corporationGreen

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel
) {
    // Observar la lista de restaurantes
    val restaurant by viewModel.restaurant.collectAsStateWithLifecycle()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "detail"

    Scaffold(
        // Ajuste edge-to-edge
        topBar = { Header { navController.navigate("profile") } },
        bottomBar = { BottomNavManager(navController, currentRoute) },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Restaurants for you",
                fontSize = 24.sp,
                color = corporationGreen,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(restaurants.size) { index ->
                    val restaurant = restaurants[index]
                    PlaceholderRestaurantCard(
                        placeName = restaurant.name,
                        productName = restaurant.products.getOrNull(0)?.productName ?: "No product available",
                        originalPrice = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0,
                        discountPrice = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0,
                        rating = restaurant.rating,
                        image = restaurant.imageUrl
                    )
                }
            }
        }
    }

}
