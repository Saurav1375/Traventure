package com.example.tripapplication.auth.domain.utils

import com.example.tripapplication.core.domain.util.Error

enum class AuthError : Error {
    AUTH_FAILED,
    TOKEN_EXCHANGE_FAILED,
    UNKNOWN,
    NO_REFRESH_TOKEN,
    DATASTORE_ERROR,
    TOKEN_REFRESH_FAILED,
}