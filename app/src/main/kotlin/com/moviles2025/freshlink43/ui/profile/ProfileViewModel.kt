package com.moviles2025.freshlink43.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val birthday: String = ""
)

class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        viewModelScope.launch {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _user.value = document.toObject(UserProfile::class.java)
                    }
                }
                .addOnFailureListener {
                    _user.value = null
                }
        }
    }
}