package com.moviles2025.freshlink43.ui.home

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.size.Size
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.ui.detail.DetailViewModel
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.corporationGreen
import com.moviles2025.freshlink43.utils.NotConnection


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    detailViewModel: DetailViewModel
) {
    val message by viewModel.welcomeMessage.collectAsStateWithLifecycle()

    val isConnected = viewModel.isConnected.collectAsState(initial = false).value

    // Cargar los restaurantes cuando se crea la pantalla
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("HomeScreen")
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
                        restaurant = restaurant,
                        viewModel = viewModel,
                        navController = navController,
                        detailViewModel = detailViewModel,// pasas el ViewModel aquí
                        onFavoriteClick = { viewModel.toggleFavorite(it) }
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
    restaurant: Restaurant,
    viewModel: HomeViewModel,
    navController: NavController,
    detailViewModel: DetailViewModel,
    onFavoriteClick: (Restaurant) -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    // 🔍 Consultamos si es favorito en un hilo de fondo
    LaunchedEffect(restaurant) {
        viewModel.isFavorite(restaurant) { result ->
            isFavorite = result
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(240.dp)
            .clickable {
                val productId = restaurant.products[0].productId
                navController.navigate("detail/${productId}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Crop
            ) // primera micro optimizacion


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = restaurant.name,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                        color = corporationGreen,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = restaurant.products.getOrNull(0)?.productName ?: "No product available",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = corporationGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = restaurant.rating.toString(),
                                fontSize = 14.sp,
                                color = corporationGreen,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.Bottom) {
                            val original = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0
                            val discount = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0

                            Text(
                                text = "$${formatAmount(discount)}",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "$${formatAmount(original)}",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                                color = corporationGreen
                            )
                        }
                    }
                }

                FavoriteIcon(
                    restaurant = restaurant,
                    viewModel = viewModel,
                    onFavoriteClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}


@Composable
fun FavoriteIcon(
    restaurant: Restaurant,
    viewModel: HomeViewModel,
    onFavoriteClick: (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(restaurant) {
        viewModel.isFavorite(restaurant) { result ->
            isFavorite = result
        }
    }

    IconButton(
        onClick = {
            isFavorite = !isFavorite
            onFavoriteClick(restaurant)
            AnalyticsManager.logRestaurantVisit(restaurant.name)
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite Icon",
            tint = Color.Red
        )
    }
}