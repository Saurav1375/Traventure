package com.example.tripapplication.auth.domain.utils


/**
 * Business error codes that match the backend's error classification.
 * These codes correspond to the BusinessErrorCodes on the server side.
 *
 * Error code groups:
 * 0-99: General errors
 * 100-199: Authentication errors
 * 200-299: Validation errors
 * 300-399: Account management errors
 * 400-499: Email and communication errors
 * 500-599: Token and session errors
 * 600-699: Permission and access errors
 * 900-999: System and unexpected errors
 */
enum class BusinessError(val code: Int, val defaultMessage: String) {
    // General errors
    NO_CODE(0, "Unspecified error occurred"),

    // Authentication errors (100-199)
    AUTHENTICATION_FAILED(100, "Authentication failed"),
    BAD_CREDENTIALS(101, "The email or password provided is incorrect"),

    // Account management errors (300-399)
    INCORRECT_CURRENT_PASSWORD(300, "The current password entered is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "New password and confirmation do not match"),
    ACCOUNT_LOCKED(302, "This account is temporarily locked due to multiple failed login attempts"),
    ACCOUNT_DISABLED(303, "This account has been disabled. Please contact support"),
    USER_NOT_FOUND(305, "No user found with the provided credentials"),
    EMAIL_ALREADY_REGISTERED(306, "An account with this email already exists"),
    ACCOUNT_NOT_VERIFIED(307, "Email address not verified. Please check your inbox"),
    ACCOUNT_ALREADY_ACTIVATED(308, "This account is already activated"),

    // Token and session errors (500-599)
    INVALID_TOKEN(500, "Authentication token is invalid"),
    EXPIRED_TOKEN(501, "Your session has expired. Please log in again"),
    MISSING_TOKEN(502, "Authentication token is required"),

    // Validation errors (200-299)
    WEAK_PASSWORD(200, "The password does not meet security requirements"),
    INVALID_EMAIL_FORMAT(201, "The email format is invalid"),

    // Rate limiting and security (600-699)
    TOO_MANY_REQUESTS(600, "Too many attempts. Please try again later"),
    UNAUTHORIZED_ACCESS(601, "You do not have permission to perform this action"),

    // Email and communication errors (400-499)
    EMAIL_SENDING_FAILED(400, "Failed to send email"),

    // System and configuration errors (700-799)
    ROLE_NOT_FOUND(700, "System role not found"),

    // Unknown and other errors (900-999)
    UNKNOWN(999, "An unknown error occurred");

    companion object {
        // Find BusinessError by code
        fun fromCode(code: Int): BusinessError {
            return entries.find { it.code == code } ?: UNKNOWN
        }
    }
}