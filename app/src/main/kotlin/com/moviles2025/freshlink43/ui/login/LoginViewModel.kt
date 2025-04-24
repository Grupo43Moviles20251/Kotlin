package com.moviles2025.freshlink43.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.moviles2025.freshlink43.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loginResult: String? = null,
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun onEmailChanged(email: String) = _uiState.update { it.copy(email = email) }
    fun onPasswordChanged(password: String) = _uiState.update { it.copy(password = password) }

    fun login(context: Context) {
        val email = uiState.value.email.trim()
        val password = uiState.value.password

        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _uiState.update { it.copy(isLoading = false) }
                showSnackbarMessage("Please enter both email and password")
                return@launch
            }

            if (!isConnected(context)) {
                showSnackbarMessage("Oops! No internet connection. Please try again later.")
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val result = repository.loginWithEmail(email, password)
            _uiState.update {
                it.copy(
                    loginSuccess = result.isSuccess,
                    loginResult = result.getOrNull() ?: result.exceptionOrNull()?.localizedMessage,
                    isLoading = false
                )
            }
        }
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun loginWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.loginWithGoogle(credential)
            _uiState.update {
                it.copy(
                    loginSuccess = result.isSuccess,
                    loginResult = result.getOrNull() ?: result.exceptionOrNull()?.localizedMessage,
                    isLoading = false
                )
            }
        }
    }

    fun clearLoginResult() {
        _uiState.update { it.copy(loginResult = null) }
    }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}