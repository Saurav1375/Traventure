package com.example.tripapplication.auth.presentation.utils

import java.util.regex.Pattern

fun isValidEmail(email: String?): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val pattern: Pattern = Pattern.compile(emailRegex)
    return email != null && pattern.matcher(email).matches()
}

// Function to check if a password is at least 6 characters long
fun isValidPassword(password: String?): Boolean {
    return password != null && password.length >= 6
}