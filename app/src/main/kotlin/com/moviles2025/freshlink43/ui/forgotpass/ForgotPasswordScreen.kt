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
import androidx.navigation.NavController
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.ui.navigation.NavRoutes

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("ForgotPasswordScreen")
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val customFont = FontFamily(Font(R.font.montserratalternates_regular))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Forgot Password",
            fontSize = 20.sp,
            color = Color(0xFF2F2F2F),
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email", fontFamily = customFont) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontFamily = customFont)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.sendPasswordReset() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38677A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Send Reset Link", fontFamily = FontFamily(Font(R.font.montserratalternates_semibold)))
        }

        uiState.resetResult?.let { message ->
            LaunchedEffect(message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Back to Login",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_extrabold)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}