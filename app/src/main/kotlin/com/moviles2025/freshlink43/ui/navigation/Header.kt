package com.moviles2025.freshlink43.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moviles2025.freshlink43.R

@Composable
fun Header(onNavigateToProfile: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 14.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier.size(50.dp)
        )

        // Solo muestra el icono si se proporciona la función
        onNavigateToProfile?.let {
            IconButton(onClick = it) {
                Image(
                    painter = painterResource(id = R.drawable.profileicon),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}