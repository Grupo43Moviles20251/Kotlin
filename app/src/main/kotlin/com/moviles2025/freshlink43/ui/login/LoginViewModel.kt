package com.moviles2025.freshlink43.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.moviles2025.freshlink43.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _loginResult = MutableLiveData<Pair<Boolean, String>>()
    val loginResult: LiveData<Pair<Boolean, String>> = _loginResult

    fun login(email: String, password: String) {
        repository.loginWithEmail(email, password) { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }

    fun loginWithGoogle(credential: AuthCredential) {
        repository.loginWithGoogle(credential) { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }
}