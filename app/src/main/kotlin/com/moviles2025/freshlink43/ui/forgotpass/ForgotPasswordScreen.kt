package com.moviles2025.freshlink43.ui.forgotpass

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Fuente personalizada usada en tu LoginScreen
    val customFont = FontFamily(Font(R.font.montserratalternates_regular))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen (logo) similar a la de Login
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        // Título principal con estilo parecido a "Login"
        Text(
            text = "Forgot Password",
            fontSize = 20.sp,
            color = Color(0xFF2F2F2F),
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de texto para Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = {
                Text(
                    "Email",
                    fontFamily = customFont
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily(Font(R.font.montserratalternates_regular))
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Botón para enviar el enlace de reseteo de contraseña
        Button(
            onClick = { viewModel.sendPasswordReset() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38677A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Send Reset Link",
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )
        }

        // Mostramos el mensaje de resultado (éxito o error) y el Toast
        uiState.resetResult?.let { message ->
            LaunchedEffect(message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto para regresar al Login
        Text(
            text = "Back to Login",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_extrabold)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onBackToLogin() }
        )
    }
}