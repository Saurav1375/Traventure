package com.example.tripapplication.auth.data.mappers

import com.example.tripapplication.auth.data.networking.dto.TokenResponseDto
import com.example.tripapplication.auth.domain.AuthResponse

fun TokenResponseDto.toAuthResponse() : AuthResponse {
    return AuthResponse(
        accessToken = access_token,
        refreshToken = refresh_token
    )
}