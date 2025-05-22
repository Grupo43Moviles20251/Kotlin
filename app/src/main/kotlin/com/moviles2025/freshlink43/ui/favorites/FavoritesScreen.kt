package com.moviles2025.freshlink43.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import coil.compose.rememberImagePainter
import coil.size.Size
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.ui.home.formatAmount
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.corporationGreen

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: "favorites"

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

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "No favorites",
                            tint = Color.LightGray,
                            modifier = Modifier.size(96.dp)
                        )
                        Text(
                            text = "Oops! No favorites yet.",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
                        )
                        Text(
                            text = "Browse restaurants and add your favorite spots.",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            fontFamily = FontFamily(Font(R.font.montserratalternates_regular))
                        )
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favorites) { restaurant ->
                        CardFavoriteRestaurant(
                            restaurant = restaurant,
                            isFavorite = true,
                            navController = navController,
                            onFavoriteClick = { viewModel.toggleFavorite(it) }
                        )
                    }

                    if (favorites.size == 1) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "More favorites",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Only one? There are more waiting for you!",
                                    fontSize = 16.sp,
                                    color = Color.LightGray,
                                    fontFamily = FontFamily(Font(R.font.montserratalternates_regular))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Search restaurants and build your favorite list :)",
                                    fontSize = 12.sp,
                                    color = Color.LightGray,
                                    fontFamily = FontFamily(Font(R.font.montserratalternates_regular))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardFavoriteRestaurant(
    restaurant: Restaurant,
    isFavorite: Boolean,
    navController: NavController,
    onFavoriteClick: (Restaurant) -> Unit
) {
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
        colors = CardDefaults.cardColors(containerColor = corporationGreen)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val painter = rememberImagePainter(
                data = restaurant.imageUrl,
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
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                    Text(
                        text = restaurant.products.getOrNull(0)?.productName ?: "No product",
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = restaurant.rating.toString(),
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.Bottom) {
                            val original = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0
                            val discount = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0

                            Text(
                                text = "$${formatAmount(discount)}",
                                fontSize = 15.sp,
                                color = Color.LightGray,
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = "$${formatAmount(original)}",
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                                color = Color.White
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { onFavoriteClick(restaurant) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Remove from favorites",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
