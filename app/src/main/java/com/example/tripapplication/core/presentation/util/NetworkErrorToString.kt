package com.example.tripapplication.core.presentation.util

import android.content.Context
import com.example.tripapplication.R
import com.example.tripapplication.auth.domain.utils.BusinessError
import com.example.tripapplication.core.domain.util.AppError
import com.example.tripapplication.core.domain.util.NetworkError

/**
 * Extension function to convert an AppError to a user-friendly error message.
 * Uses localized strings from resources where available.
 */
fun AppError.toUserMessage(context: Context): String {
    return when (this) {
        is AppError.Network -> {
            val networkError = this.error
            val resId = when (networkError) {
                NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
                NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
                NetworkError.NO_INTERNET -> R.string.error_no_internet
                NetworkError.SERVER_ERROR -> R.string.error_server
                NetworkError.SERIALIZATION -> R.string.error_serialization
                NetworkError.UNKNOWN -> R.string.error_unknown
            }
            context.getString(resId)
        }
        is AppError.Business -> {
            // Use the custom message from the server if available
            context.getString(getBusinessErrorStringResource(this.error))
        }
        is AppError.Validation -> {
            // For validation errors, return the first field error or a general validation error message
            this.fieldErrors?.values?.firstOrNull() ?:
            context.getString(getBusinessErrorStringResource(this.error))
        }
    }
}

/**
 * Maps a BusinessError to a string resource ID.
 * This allows for localized error messages for business errors.
 */
private fun getBusinessErrorStringResource(error: BusinessError): Int {
    return when (error) {
        BusinessError.AUTHENTICATION_FAILED -> R.string.error_authentication_failed
        BusinessError.BAD_CREDENTIALS -> R.string.error_bad_credentials
        BusinessError.USER_NOT_FOUND -> R.string.error_user_not_found
        BusinessError.EMAIL_ALREADY_REGISTERED -> R.string.error_email_already_registered
        BusinessError.ACCOUNT_LOCKED -> R.string.error_account_locked
        BusinessError.ACCOUNT_DISABLED -> R.string.error_account_disabled
        BusinessError.ACCOUNT_NOT_VERIFIED -> R.string.error_account_not_verified
        BusinessError.EXPIRED_TOKEN -> R.string.error_session_expired
        BusinessError.INVALID_TOKEN -> R.string.error_invalid_token
        BusinessError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        BusinessError.UNAUTHORIZED_ACCESS -> R.string.error_unauthorized
        BusinessError.WEAK_PASSWORD -> R.string.error_weak_password
        BusinessError.NEW_PASSWORD_DOES_NOT_MATCH -> R.string.error_passwords_dont_match
        // Add mappings for other business errors
        else -> R.string.error_unknown
    }
}