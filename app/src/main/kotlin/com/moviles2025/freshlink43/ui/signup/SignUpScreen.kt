package com.moviles2025.freshlink43.ui.signup

import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moviles2025.freshlink43.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val montserratBold = FontFamily(Font(R.font.montserratalternates_bold))
    val montserratRegular = FontFamily(Font(R.font.montserratalternates_regular))
    val montserratSemiBold = FontFamily(Font(R.font.montserratalternates_semibold))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado consistente
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoapp),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Sign Up",
                fontFamily = montserratBold,
                fontSize = 20.sp,
                color = Color(0xFF2F2F2F)
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChanged(it) },
                label = { Text("Name", fontFamily = montserratRegular) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = montserratRegular),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email", fontFamily = montserratRegular) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontFamily = montserratRegular),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically) // Asegura alineación vertical
                ) {
                    DatePickerField(
                        selectedDate = uiState.birthday,
                        onDateSelected = { viewModel.onBirthdayChanged(it) }
                    )
                }

                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { viewModel.onAddressChanged(it) },
                    label = { Text("Address", fontFamily = montserratRegular) },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontFamily = montserratRegular),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically) // Asegura alineación vertical
                )
            }


            PasswordField(
                label = { Text("Password", fontFamily = montserratRegular) },
                password = uiState.password,
                onPasswordChange = { viewModel.onPasswordChanged(it) }
            )

            PasswordField(
                label = { Text("Confirm Password", fontFamily = montserratRegular) },
                password = uiState.confirmPassword,
                onPasswordChange = { viewModel.onConfirmPasswordChanged(it) },
                error = uiState.confirmPasswordError
            )

            Button(
                onClick = { viewModel.signUp(context) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38677A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up", fontFamily = montserratSemiBold)
            }

            Text(
                text = "Already have an account?",
                color = Color(0xFF38677A),
                fontFamily = montserratRegular
            )

            Text(
                text = "Log In",
                color = Color(0xFF38677A),
                fontFamily = montserratSemiBold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }

    uiState.signUpResult?.let { result ->
        LaunchedEffect(result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            if (uiState.signUpSuccess) {
                onNavigateToLogin()
            }
        }
    }
}

@Composable
fun PasswordField(
    label: @Composable () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val montserratRegular = FontFamily(Font(R.font.montserratalternates_regular))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = label,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontFamily = montserratRegular),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        isError = error != null
    )
    if (error != null) {
        Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
    }
}



@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDateFormatted =
                    String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(selectedDateFormatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp)
                .clickable { datePickerDialog.show() }, // Simula el clic en un TextField
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (selectedDate.isEmpty()) "Select Birthday" else selectedDate,
                    color = if (selectedDate.isEmpty()) Color.Gray else Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserratalternates_regular))
                )
            }
        }
    }
}
