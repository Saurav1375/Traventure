package com.example.tripapplication.auth.presentation.login

sealed interface AuthEvent {
    data object LoginSuccess : AuthEvent
    data object RegisterSuccess : AuthEvent
    data object ActivationSuccess : AuthEvent
    data object ForgetPassSuccess : AuthEvent
    data object ResendCodeSuccess : AuthEvent

    data class Error(val message: String) : AuthEvent
}