package com.moviles2025.freshlink43.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
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
            text = "Login",
            fontSize = 20.sp,
            color = Color(0xFF2F2F2F),
            fontFamily = FontFamily(Font(R.font.montserratalternates_bold)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text(
                "Email",
                fontFamily = customFont
            ) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily(Font(R.font.montserratalternates_regular)))
        )

        Spacer(modifier = Modifier.height(10.dp))

        var passwordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text(
                "Password",
                fontFamily = customFont
            ) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily(Font(R.font.montserratalternates_regular))),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Forgot your password?",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_medium)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    Toast.makeText(context, "Redirect to recover password", Toast.LENGTH_SHORT).show()
                    onNavigateToForgotPassword()
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.login(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38677A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Sign In",
                fontFamily = FontFamily(Font(R.font.montserratalternates_semibold))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Or you can",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_medium)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GoogleSignInButton(onClick = onGoogleSignIn)

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            "Never Experienced FreshLink?",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_medium)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            "Sign Up",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserratalternates_extrabold)),
            color = Color(0xFF38677A),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onNavigateToSignUp() }
        )

        uiState.loginResult?.let { result ->
            LaunchedEffect(result) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                if (uiState.loginSuccess) onLoginSuccess()
            }
        }
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.glogo), // Agrega el logo de Google en tus resources
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign in with Google",
                color = Color(0xFF38677A),
                fontFamily = FontFamily(Font(R.font.montserratalternates_medium))
            )
        }
    }
}
