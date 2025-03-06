package com.moviles2025.freshlink43.ui.signup

import androidx.lifecycle.ViewModel
import com.moviles2025.freshlink43.data.repository.SignUpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun signUp() {
        val state = _uiState.value
        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(confirmPasswordError = "Passwords do not match")
            return
        }

        repository.signUpWithEmail(state.email, state.password) { success, message ->
            _uiState.value = _uiState.value.copy(
                signUpResult = message,
                signUpSuccess = success
            )
        }
    }
}

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val signUpResult: String? = null,
    val signUpSuccess: Boolean = false
)