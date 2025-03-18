package com.moviles2025.freshlink43.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.home.HomeActivity
import com.moviles2025.freshlink43.ui.profile.ProfileActivity
//import com.moviles2025.freshlink43.ui.search.SearchActivity
import com.moviles2025.freshlink43.ui.ubication.UbicationActivity

@Composable
fun BottomNavManager(
    context: Context,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        R.drawable.restaurante, // Home
        R.drawable.corazon,     // Favoritos o Perfil
        R.drawable.busqueda,    // Búsqueda
        R.drawable.marcador     // Ubicación
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = {
                    onTabSelected(index)
                    navigateToScreen(index, context)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
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

private fun navigateToScreen(index: Int, context: Context) {
    when (index) {
        0 -> context.startActivity(Intent(context, HomeActivity::class.java))
        //1 -> context.startActivity(Intent(context, ProfileActivity::class.java))
        //2 -> context.startActivity(Intent(context, SearchActivity::class.java))
        3 -> context.startActivity(Intent(context, UbicationActivity::class.java))
    }
}