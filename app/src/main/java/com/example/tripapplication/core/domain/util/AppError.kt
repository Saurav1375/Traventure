package com.example.tripapplication.core.domain.util

import com.example.tripapplication.auth.domain.utils.BusinessError

/**
 * Represents all possible error types in the application.
 * This includes both network errors and specific business errors from the backend.
 */
sealed class AppError : Error{
    // Network-related errors
    data class Network(val error: NetworkError) : AppError()
    
    // Business-related errors from the backend
    data class Business(
        val error: BusinessError,
        val message: String? = null
    ) : AppError()
    
    // Specific validation errors
    data class Validation(
        val fieldErrors: Map<String, String>? = null,
        val error: BusinessError
    ) : AppError()
}