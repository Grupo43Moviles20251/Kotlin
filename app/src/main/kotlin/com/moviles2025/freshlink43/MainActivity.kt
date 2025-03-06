package com.moviles2025.freshlink43

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onLoginClick = {
                    startActivity(Intent(this, com.moviles2025.freshlink43.ui.login.LoginActivity::class.java))
                },
                onSignUpClick = {
                    startActivity(Intent(this, com.moviles2025.freshlink43.ui.signup.SignUpActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto de bienvenida
        Text(
            text = "Welcome to FreshLink",
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
            fontSize = 24.sp,
            color = Color(0xFF38677A)
        )

        // Botones abajo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onLoginClick,
                shape = RoundedCornerShape(16.dp), // Si quieres bordes redondeados
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "Log In",
                    fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
                )
            }

            Button(
                onClick = onSignUpClick,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Sign Up",
                    fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
                )
            }
        }
    }
}