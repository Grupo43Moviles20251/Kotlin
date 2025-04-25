package com.moviles2025.freshlink43.ui.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.repository.ProfileRepository
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val birthday: String = "",
    val photoUrl: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val _photoUrl = MutableStateFlow<String?>(null)
    val photoUrl: StateFlow<String?> = _photoUrl

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile()
                .onSuccess { profile ->
                    _user.value = profile
                    _photoUrl.value = profile?.photoUrl
                }
                .onFailure {
                    _user.value = null
                    _photoUrl.value = null
                }
        }
    }

    fun uploadPhoto(uri: Uri) {
        viewModelScope.launch {
            repository.uploadProfileImage(uri)
                .onSuccess { newUrl ->
                    _photoUrl.value = newUrl
                    // puedes recargar perfil si quieres sincronizar más datos
                    loadUserProfile()
                }
                .onFailure {
                    println("Error uploading image: ${it.localizedMessage}")
                }
        }
    }

    fun signOut() {
        repository.signOut()
        _user.value = null
        _photoUrl.value = null
    }

    fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}