package com.moviles2025.freshlink43.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.moviles2025.freshlink43.R

@Composable
fun MainScreen(navController: NavController) {
    val buttonColor = Color(0xFF38677A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // 🔹 Logo
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Texto de bienvenida
        Text(
            text = "Welcome to FreshLink",
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
            fontSize = 24.sp,
            color = Color(0xFF38677A)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 Botones de Login y SignUp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("login") }, // 🔄 Usa NavController
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = Color.White),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Log In", fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
            }

            Button(
                onClick = { navController.navigate("signup") }, // 🔄 Usa NavController
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = Color.White),
                modifier = Modifier.weight(1f)
            ) {
                Text("Sign Up", fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
            }
        }
    }
}