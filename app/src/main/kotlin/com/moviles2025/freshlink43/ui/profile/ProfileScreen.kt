package com.moviles2025.freshlink43.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.utils.*
import com.moviles2025.freshlink43.ui.navigation.NavRoutes

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.profileicon), // Imagen de perfil por defecto
            contentDescription = "User Profile",
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )

        Text(
            text = user?.name ?: "User",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Email: ${user?.email ?: "No email"}",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
        )

        Text(
            text = "Address: ${user?.address ?: "No address"}",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack() // Regresa a la pantalla anterior
            },
            colors = ButtonDefaults.buttonColors(containerColor = corporationGreen)
        ) {
            Text(
                "Go back",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut() // Cierra sesión
                navController.navigate(NavRoutes.Main.route) {
                    popUpTo(NavRoutes.Home.route) { inclusive = true } // Elimina Home del historial
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = corporationOrange)
        ) {
            Text("Log Out",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))

        }
    }
}