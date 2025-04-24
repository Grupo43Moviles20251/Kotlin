package com.moviles2025.freshlink43.ui.search

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
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
import com.moviles2025.freshlink43.model.Restaurant
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.utils.*
import com.moviles2025.freshlink43.utils.corporationGreen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {
    val query = remember { mutableStateOf(TextFieldValue("")) }
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "search"

    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("SearchScreen")
    }

    LaunchedEffect(query.value.text) {
        viewModel.getFilteredRestaurants(query.value.text)
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
            SearchBar(query)

            FilterButtons(viewModel)

            Text(
                text = "Last Offers",
                fontSize = 24.sp,
                color = corporationGreen,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = corporationGreen)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    if (restaurants.isEmpty()) {
                        viewModel.getAllRestaurants()
                    } else {
                        items(restaurants.size) { index ->
                            val restaurant = restaurants[index]
                            PlaceholderRestaurantCard(
                                restaurant = restaurant,
                                navController = navController
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SearchBar(query: MutableState<TextFieldValue>) {
    TextField(
        value = query.value,
        onValueChange = { query.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        placeholder = {
            Text(
                text = "Search",
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search Icon")
        },
        shape = MaterialTheme.shapes.medium

    )
}

@Composable
fun FilterButtons(viewModel: SearchViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        FilterButton("Café", R.drawable.iconoseacrh) { viewModel.getFilteredRestaurantsByType("1") }
        FilterButton("Restaurants", R.drawable.iconorest) { viewModel.getFilteredRestaurantsByType("2") }
        FilterButton("Supermarkets", R.drawable.iconosuper) { viewModel.getFilteredRestaurantsByType("3") }
    }
}

@Composable
fun FilterButton(text: String, image: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = corporationGreen),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                fontSize = 20.sp
            )
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
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(220.dp)
            .clickable {
                val productId = restaurant.products[0].productId
                navController.navigate("detail/${productId}")
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val painter = rememberImagePainter(data = restaurant.imageUrl, builder = { size(Size.ORIGINAL) })
            Image(
                painter = painter,
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                    color = corporationGreen
                )

                Text(
                    text = restaurant.products.getOrNull(0)?.productName ?: "No product available",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = "Rating", tint = corporationGreen)
                        Text(text = restaurant.rating.toString(), fontSize = 14.sp, color = corporationGreen)
                    }

                    Row(verticalAlignment = Alignment.Bottom) {
                        val discount = restaurant.products.getOrNull(0)?.discountPrice?.toInt() ?: 0
                        val original = restaurant.products.getOrNull(0)?.originalPrice?.toInt() ?: 0

                        Text(
                            text = "$${formatAmount(discount)}",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
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
        }
    }
}