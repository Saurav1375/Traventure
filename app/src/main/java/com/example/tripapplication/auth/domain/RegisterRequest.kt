package com.example.tripapplication.auth.domain

data class RegisterRequest (
    val firstname : String,
    val lastname : String,
    val email : String,
    val password : String,
)