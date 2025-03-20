package com.moviles2025.freshlink43.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.SignUpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
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

    fun signUp(context: Context) {
        val state = _uiState.value

        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(confirmPasswordError = "Passwords do not match")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true) // Activar carga

        viewModelScope.launch {
            repository.signUpWithEmail(
                context = context,
                name = state.name,
                email = state.email,
                password = state.password,
                address = state.address,
                birthday = state.birthday
            ) { success, message ->
                _uiState.value = _uiState.value.copy(
                    signUpResult = message,
                    signUpSuccess = success,
                    isLoading = false // Desactivar carga al terminar
                )
            }
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
    val signUpResult: String? = null,
    val signUpSuccess: Boolean = false,
    val isLoading: Boolean = false // Estado de carga
)