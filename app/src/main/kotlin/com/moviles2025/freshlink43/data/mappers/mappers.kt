package com.moviles2025.freshlink43.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.moviles2025.freshlink43.data.dto.UserDto

fun FirebaseUser.toDto(): UserDto {
    return UserDto(
        name = this.displayName ?: "Unknown",
        email = this.email ?: "",
        password = "google_auth",
        address = "Unknown",
        birthday = "2000-01-01",
        //photoUrl = this.photoUrl?.toString()
    )
}