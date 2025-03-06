package com.moviles2025.freshlink43.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles2025.freshlink43.data.repository.SignUpRepository

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()

    private val _signUpResult = MutableLiveData<Pair<Boolean, String>>()
    val signUpResult: LiveData<Pair<Boolean, String>> = _signUpResult

    fun signUp(email: String, password: String) {
        repository.signUpWithEmail(email, password) { success, message ->
            _signUpResult.postValue(Pair(success, message))
        }
    }
}