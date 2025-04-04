package com.example.tripapplication.auth.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(
    val access_token: String,
    val refresh_token: String,
)