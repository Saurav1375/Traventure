package com.example.tripapplication.core.domain.util

import kotlinx.serialization.Serializable
/**
 * Response structure for error responses from the backend.
 * Maps to the server's exception response format.
 */
@Serializable
data class ErrorResponse(
    val businessErrorCode: Int,
    val businessErrorDescription: String,
    val error: String,
    val errors: Map<String, String>? = null,
    val validationErrors: List<String>? = null
)