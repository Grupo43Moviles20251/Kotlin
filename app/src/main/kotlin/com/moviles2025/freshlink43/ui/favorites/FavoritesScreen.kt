package com.moviles2025.freshlink43.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.size.Size
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.ui.home.formatAmount
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.corporationGreen
import com.moviles2025.freshlink43.utils.corporationBlue

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "favorites"

    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("FavoritesScreen")
    }

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
            Text(
                text = "Favorites",
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
                items(favorites) { restaurant ->
                    CardFavoriteRestaurant(
                        placeName = restaurant.name,
                        productName = restaurant.products.getOrNull(0)?.productName ?: "No product",
                        originalPrice = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0,
                        discountPrice = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0,
                        rating = restaurant.rating,
                        image = restaurant.imageUrl,
                        isFavorite = true,
                        onFavoriteClick = {
                            //viewModel.removeFavorite(restaurant) // 🔥 Aquí llamas tu ViewModel para quitarlo
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CardFavoriteRestaurant(
    placeName: String,
    productName: String,
    originalPrice: Int,
    discountPrice: Int,
    rating: Double,
    image: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(240.dp), // Aumentamos un poquito el alto
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = corporationGreen)
    ) {
        Box {
            Column(modifier = Modifier.fillMaxSize()) {
                val painter = rememberImagePainter(
                    data = image,
                    builder = {
                        size(Size.ORIGINAL)
                        crossfade(true)
                    }
                )
                Box {
                    Image(
                        painter = painter,
                        contentDescription = "Restaurant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )

                    IconButton(
                        onClick = { onFavoriteClick() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite Icon",
                            tint = Color.Red
                        )
                    }
                }

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
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                    Text(
                        text = productName,
                        fontSize = 14.sp,
                        color = Color.LightGray
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
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = rating.toString(),
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$${formatAmount(discountPrice)}",
                                fontSize = 15.sp,
                                color = Color.LightGray,
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "$${formatAmount(originalPrice)}",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}