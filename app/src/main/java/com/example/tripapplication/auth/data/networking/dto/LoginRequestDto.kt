package com.example.tripapplication.auth.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto (
    val email : String,
    val password : String,
)