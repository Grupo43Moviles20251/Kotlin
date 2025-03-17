package com.moviles2025.freshlink43.ui.home

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToProfile: () -> Unit
) {
    val message by viewModel.welcomeMessage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(onNavigateToProfile)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f) // Color gris con transparencia
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // Centra la imagen horizontalmente
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoapp),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp) // Establece el tamaño de la imagen
            )
        }

        Text(
            text = "Restaurants for you",
            fontSize = 22.sp,
            color = Color(0xFF2A9D8F),
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
            modifier = Modifier.padding(bottom = 8.dp)
                .fillMaxWidth() // Hace que ocupe todo el ancho disponible
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(top = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(2) { index ->
                PlaceholderRestaurantCard(index)
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun Header(onNavigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier.size(50.dp)
        )
        IconButton(onClick = { onNavigateToProfile() }) {
            Image(
                painter = painterResource(id = R.drawable.profileicon),
                contentDescription = "Profile Icon",
                modifier = Modifier.size(42.dp)

            )
        }
    }
}


@Composable
fun PlaceholderRestaurantCard(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(220.dp), // Altura ajustada
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)) // Fondo suave
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen en la parte superior
            Image(
                painter = painterResource(id = R.drawable.bakery3), // Asegúrate de que la imagen exista en drawable
                contentDescription = "Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Redondear solo la parte superior
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Para que el contenido se expanda
                    .padding(16.dp)
            ) {
                Text(
                    text = if (index == 0) "Café Pasaje" else "Super Organico",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                    color = Color(0xFF2A9D8F),
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                )

                Text(
                    text = "Surprise bag",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f)) // Empuja los elementos hacia abajo

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp), // Asegura alineación en la parte inferior
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom // 🔹 Alinea todo en la misma altura
                ) {
                    // ⭐ Sección de Rating con Icono
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star, // Icono de estrella de Material
                            contentDescription = "Rating",
                            tint = Color(0xFF2A9D8F),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "4.8",
                            fontSize = 14.sp,
                            color = Color(0xFF2A9D8F),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    // 💰 Sección de Precios
                    Row(
                        verticalAlignment = Alignment.Bottom // 🔹 Se asegura de que los textos estén alineados
                    ) {
                        // Precio tachado con `TextDecoration.LineThrough`
                        Text(
                            text = "$18.50k",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        // Precio final en verde
                        Text(
                            text = if (index == 0) "$5.00k" else "$7.00k",
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

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    val items = listOf(R.drawable.restaurante, R.drawable.corazon, R.drawable.busqueda, R.drawable.marcador)
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,  // 🔹 Elimina la sombra de selección
                    selectedIconColor = Color(0xFF38677A),  // 🔹 Color del icono seleccionado
                    unselectedIconColor = Color.Gray  // 🔹 Color del icono sin seleccionar
                )
            )
        }
    }
}