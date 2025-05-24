package com.moviles2025.freshlink43.ui.signup

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.data.AnalyticsManager
import com.moviles2025.freshlink43.utils.*
import com.moviles2025.freshlink43.utils.corporationBlack
import com.moviles2025.freshlink43.utils.corporationBlue
import java.util.Calendar

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel
) {
    LaunchedEffect(Unit) {
        AnalyticsManager.logFeatureUsage("SignUpScreen")
    }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val montserratBold = FontFamily(Font(R.font.montserratalternates_bold))
    val montserratRegular = FontFamily(Font(R.font.montserratalternates_regular))
    val montserratSemiBold = FontFamily(Font(R.font.montserratalternates_semibold))
    val state = uiState
    val nameValid     = state.name.isNotBlank()
    val emailValid    = Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
    val passValid     = state.password.length >= 6
    val confirmValid  = state.confirmPassword == state.password
    val addressValid  = state.address.isNotBlank()
    val birthdayValid = state.birthday.isNotBlank()

    val canSignUp = !state.isLoading &&
            nameValid && emailValid &&
            passValid && confirmValid &&
            addressValid && birthdayValid


    // Observa el snackbarMessage y muestra el Snackbar cuando haya mensaje
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    // Scaffold para soportar Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp), // Padding interior para contenido
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = corporationBlue)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Registering user...",
                        fontSize = 16.sp,
                        fontFamily = montserratSemiBold,
                        color = corporationBlue
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        color = corporationBlack
                    )

                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = { if (it.length <= 35) viewModel.onNameChanged(it) },
                        label = { Text("Name", fontFamily = montserratRegular) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { if (it.length <= 50) viewModel.onEmailChanged(it) },
                        label = { Text("Email", fontFamily = montserratRegular) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.emailError != null
                    )
                    val emailErrorText = state.emailError ?: if (state.email.isNotEmpty() && !emailValid)
                        "Formato de correo inválido" else null

                    emailErrorText?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp)
                        )
                    }

                    if (uiState.emailError != null) {
                        Text(
                            text = uiState.emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        ) {
                            DatePickerField(
                                selectedDate = uiState.birthday,
                                onDateSelected = { viewModel.onBirthdayChanged(it) }
                            )
                        }

                        OutlinedTextField(
                            value = uiState.address,
                            onValueChange = { if (it.length <= 30) viewModel.onAddressChanged(it) },
                            label = { Text("Address", fontFamily = montserratRegular) },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontFamily = montserratRegular),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )
                    }

                    PasswordField(
                        label = { Text("Password", fontFamily = montserratRegular) },
                        password = uiState.password,
                        onPasswordChange = { if (it.length <= 30) viewModel.onPasswordChanged(it) }
                    )

                    PasswordField(
                        label = { Text("Confirm Password", fontFamily = montserratRegular) },
                        password = uiState.confirmPassword,
                        onPasswordChange = { if (it.length <= 30) viewModel.onConfirmPasswordChanged(it) },
                        error = uiState.confirmPasswordError
                    )

                    Button(
                        onClick = { viewModel.signUp(context) },
                        enabled = canSignUp,
                        shape   = RoundedCornerShape(16.dp),
                        modifier= Modifier.fillMaxWidth(),
                        colors  = ButtonDefaults.buttonColors(containerColor = corporationBlue)
                    ) {
                        Text("Sign Up", fontFamily = montserratSemiBold)
                    }

                    Text(
                        text = "Already have an account?",
                        color = corporationBlue,
                        fontFamily = montserratRegular
                    )

                    Text(
                        text = "Log In",
                        color = corporationBlue,
                        fontFamily = montserratSemiBold,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                }
            }
        }
    }

    // En vez de Toast, muestra resultado via Snackbar usando ViewModel
    uiState.signUpResult?.let { result ->
        LaunchedEffect(result) {
            viewModel.showSnackbarMessage(result)
            if (uiState.signUpSuccess) {
                navController.popBackStack()
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

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = label,
        singleLine = true,
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

    // Restamos 15 años
    val maxDateCalendar = Calendar.getInstance().apply {
        add(Calendar.YEAR, -15)
    }

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
        ).apply {
            datePicker.maxDate = maxDateCalendar.timeInMillis
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp)
                .clickable { datePickerDialog.show() },
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