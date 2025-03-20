package com.moviles2025.freshlink43.ui.forgotpass

import androidx.lifecycle.ViewModel
import com.moviles2025.freshlink43.data.repository.ForgotPasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ForgotPasswordUiState(
    val email: String = "",
    val resetResult: String? = null,
    val resetSuccess: Boolean = false
)


class ForgotPasswordViewModel : ViewModel() {

    private val repository = ForgotPasswordRepository()

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun sendPasswordReset() {
        val email = _uiState.value.email
        repository.sendPasswordResetEmail(email) { success, message ->
            _uiState.update { it.copy(resetResult = message, resetSuccess = success) }
        }
    }
}