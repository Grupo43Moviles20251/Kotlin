package com.moviles2025.freshlink43.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles2025.freshlink43.data.repository.SignUpRepository
import com.moviles2025.freshlink43.data.model.SignUpResult
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password)
            _signUpResult.postValue(result)
        }
    }
}