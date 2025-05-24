package com.moviles2025.freshlink43.ui.signup

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: SignUpRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun onAddressChanged(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
    }

    fun onBirthdayChanged(birthday: String) {
        _uiState.value = _uiState.value.copy(birthday = birthday)
    }


    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun signUp(context: Context) {
        val state = _uiState.value
        val name = state.name.trim()
        val email = state.email.trim()
        val pass = state.password
        val confirm = state.confirmPassword
        val address = state.address.trim()
        val birthday = state.birthday

        // 1. Campos obligatorios
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() ||
            confirm.isEmpty() || address.isEmpty() || birthday.isEmpty()
        ) {
            showSnackbarMessage("Please complete all the spaces")
            return
        }

        // 2. Formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = state.copy(emailError = "Invalid Email")
            return
        }

        // 3. Longitud mínima de contraseña
        if (pass.length < 6) {
            showSnackbarMessage("The password should have at least 6 characters")
            return
        }

        // 4. Confirmación de contraseña
        if (pass != confirm) {
            _uiState.value = state.copy(confirmPasswordError = "Passwords do not match")
            return
        }

        // 5. Conectividad
        if (!isConnected(context)) {
            showSnackbarMessage("Oops! No internet connection. Please try again later.")
            return
        }

        _uiState.value = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = repository.signUpWithEmail(
                name = name,
                email = email,
                password = pass,
                address = address,
                birthday = birthday
            )

            val errorMsg = result.exceptionOrNull()?.localizedMessage
            val exists = errorMsg?.contains("ya está registrado", true) == true

            _uiState.value = _uiState.value.copy(
                signUpSuccess = result.isSuccess,
                signUpResult = if (result.isSuccess) "Usuario registrado correctamente!"
                else if (!exists) errorMsg
                else null,
                emailError = if (exists) errorMsg else null,
                isLoading = false
            )
        }
    }
}

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val address: String = "",
    val birthday: String = "",
    val confirmPasswordError: String? = null,
    val emailError: String? = null,
    val signUpResult: String? = null,
    val signUpSuccess: Boolean = false,
    val isLoading: Boolean = false // Estado de carga
)
