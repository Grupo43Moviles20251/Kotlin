package com.moviles2025.freshlink43.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _welcomeMessage = MutableStateFlow("Welcome to FreshLink!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage

    // Ejemplo: Si más adelante quieres cargar datos
    fun loadWelcomeMessage() {
        viewModelScope.launch {
            _welcomeMessage.value = "Welcome to FreshLink - Enjoy your food journey!"
        }
    }
}