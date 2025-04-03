package com.example.tripapplication.auth.presentation.auth_screen


data class AuthState(
    val isAuthenticated: Boolean = false,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val idToken: String? = null,
    val accessTokenExpirationTime: Long? = null
)