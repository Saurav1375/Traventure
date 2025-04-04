package com.example.tripapplication.auth.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto (
    val firstname : String,
    val lastname : String,
    val email : String,
    val password : String,
)