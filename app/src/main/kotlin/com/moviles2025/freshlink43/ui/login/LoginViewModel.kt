package com.moviles2025.freshlink43.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.moviles2025.freshlink43.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loginResult: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChanged(email: String) = _uiState.update { it.copy(email = email) }
    fun onPasswordChanged(password: String) = _uiState.update { it.copy(password = password) }

    fun login(context: Context) {  // 👈 Agregar Context como parámetro
        repository.loginWithEmail(uiState.value.email, uiState.value.password, context) { success, message ->
            _uiState.update { it.copy(loginResult = message, loginSuccess = success) }
        }
    }

    fun loginWithGoogle(credential: AuthCredential, context: Context) {  // 👈 Agregar Context
        repository.loginWithGoogle(credential, context) { success, message ->
            _uiState.update { it.copy(loginResult = message, loginSuccess = success) }
        }
    }
}