package com.moviles2025.freshlink43.ui.home

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import coil.compose.rememberImagePainter
import coil.size.Size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.moviles2025.freshlink43.ui.navigation.BottomNavManager
import com.moviles2025.freshlink43.ui.navigation.Header
import com.moviles2025.freshlink43.ui.utils.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToProfile: () -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,

) {
    val context = LocalContext.current
    val message by viewModel.welcomeMessage.collectAsStateWithLifecycle()
    // Cargar los restaurantes cuando se crea la pantalla
    LaunchedEffect(Unit) {
        viewModel.getRestaurants(context)
    }

    // Observar la lista de restaurantes
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Header(onNavigateToProfile)

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )

        Text(
            text = "Restaurants for you",
            fontSize = 24.sp,
            color = corporationGreen,
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
            modifier = Modifier.padding(bottom = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(top = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // Recorre la lista de restaurantes usando un for y crea una tarjeta para cada uno
            items(restaurants.size) { index ->
                val restaurant = restaurants[index] // Obtiene el restaurante de la lista
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

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )

        BottomNavManager(
            context = LocalContext.current,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
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
            .padding(10.dp)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    modifier = Modifier
                        .padding(bottom = 5.dp)
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
                    // ⭐ Sección de Rating con Icono
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
                        // Precio tachado
                        Text(
                            text = "$${formatAmount(discountPrice)}",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        // Precio final en verde
                        Text(
                            text = "$${formatAmount(originalPrice)}",
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                            color = Color(0xFF2A9D8F)
                        )
                    }
                }
            }
        }
    }
}

