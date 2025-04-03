package com.example.tripapplication.auth.domain

data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val idToken: String? = null,
    val accessTokenExpirationTime: Long? = null
)