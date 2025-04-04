package com.example.tripapplication.auth.presentation.login


sealed interface AuthAction {
    data class OnLoginEmailChange(val email : String) : AuthAction
    data class OnLoginPasswordChange(val password: String) : AuthAction
    data class OnRegisterNameChange(val firstname: String) : AuthAction
    data class OnRegisterEmailChange(val email: String) : AuthAction
    data class OnRegisterPasswordChange(val password: String) : AuthAction

    data object Login : AuthAction
    data object Register : AuthAction

    data object Logout : AuthAction

    data class OnEnterNumber(val number: Int?, val index: Int): AuthAction
    data class OnChangeFieldFocused(val index: Int): AuthAction
    data object OnKeyboardBack: AuthAction

    data object OnForgotPassword : AuthAction
    data object OnReSendCode : AuthAction


}