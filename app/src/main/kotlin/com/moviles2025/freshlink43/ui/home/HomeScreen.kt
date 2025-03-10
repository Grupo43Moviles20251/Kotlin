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

        Text(
            text = "Restaurants for you",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
            modifier = Modifier.padding(bottom = 8.dp)
                .fillMaxWidth() // Hace que ocupe todo el ancho disponible
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
            color = Color.Gray.copy(alpha = 0.3f) // Color gris con transparencia
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
            .padding(8.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (index == 0) "Café Pasaje" else "Super Organico",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
                color = Color(0xFF38677A)
            )
            Text(
                text = "Surprise bag",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (index == 0) "$5.00k" else "$7.00k",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)),
                color = Color(0xFF38677A)
            )
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