package com.example.tripapplication.auth.domain

data class AuthResponse(
    val email: String? = null,
    val isActivated: Boolean = true,
    val accessToken: String? = null,
    val refreshToken: String? = null,
)