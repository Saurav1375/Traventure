package com.example.tripapplication.auth.presentation.login

import com.example.tripapplication.auth.domain.RegisterRequest
import com.example.tripapplication.auth.domain.utils.LoginRequest


data class AuthState(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isLoading: Boolean = false,
    val email: String? = null,
    val isActivated: Boolean = true,
    val loginRequest: LoginRequest = LoginRequest("", ""),
    val registerRequest: RegisterRequest = RegisterRequest("", "", "", ""),
    val code: List<Int?> = (1..4).map { null },
    val focusedIndex: Int? = null,
)

