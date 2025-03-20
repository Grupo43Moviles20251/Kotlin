package com.moviles2025.freshlink43.ui.home

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.ui.utils.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val message by viewModel.welcomeMessage.collectAsStateWithLifecycle()

    // Cargar los restaurantes cuando se crea la pantalla
    LaunchedEffect(Unit) {
        viewModel.getRestaurants()
    }

    // Observar la lista de restaurantes
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"

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
                        productName = restaurant.products[0].productName,
                        originalPrice = restaurant.products[0].originalPrice.toInt(),
                        discountPrice = restaurant.products[0].discountPrice.toInt(),
                        rating = restaurant.rating,
                        image = restaurant.imageUrl
                    )
                }
            }
        }
    }
}

fun formatAmount(amount: Int): String {
    val formatter = DecimalFormat("#,###")
    formatter.decimalFormatSymbols = formatter.decimalFormatSymbols.apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    return formatter.format(amount)
}

@Composable
fun PlaceholderRestaurantCard(
    placeName: String,
    productName: String,
    originalPrice: Int,
    discountPrice: Int,
    rating: Double,
    image: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val painter = rememberImagePainter(
                data = image,
                builder = {
                    size(Size.ORIGINAL)
                    crossfade(true)
                }
            )
            Image(
                painter = painter,
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = placeName,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                    color = corporationGreen,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Text(
                    text = productName,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = corporationGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = rating.toString(),
                            fontSize = 14.sp,
                            color = corporationGreen,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$${formatAmount(discountPrice)}",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = "$${formatAmount(originalPrice)}",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                            color = corporationGreen
                        )
                    }
                }
            }
        }
    }
}