package com.moviles2025.freshlink43.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToProfile: () -> Unit
) {
    val message by viewModel.welcomeMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
        )

        Button(
            onClick = {
                Toast.makeText(context, "Going to Profile", Toast.LENGTH_SHORT).show()
                onNavigateToProfile()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38677A))
        ) {
            Text("Go to Profile", fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
        }
    }
}