package com.moviles2025.freshlink43.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moviles2025.freshlink43.R

@Composable
fun BottomNavManager(
    navController: NavController,
    selectedTab: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("home", R.drawable.restaurante), // Home
        BottomNavItem("favorites", R.drawable.corazon), // Favoritos
        BottomNavItem("search", R.drawable.busqueda),   // Búsqueda
        BottomNavItem("ubication", R.drawable.marcador) // Ubicación
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.route,
                onClick = {
                    if (selectedTab != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp) // Aumenta el tamaño del ícono (antes 24.dp)
                            .padding(4.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color(0xFF38677A),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: Int)