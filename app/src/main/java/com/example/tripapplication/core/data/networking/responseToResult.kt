package com.example.tripapplication.core.data.networking

import android.util.Log
import com.example.tripapplication.auth.domain.utils.BusinessError
import com.example.tripapplication.core.domain.util.AppError
import com.example.tripapplication.core.domain.util.ErrorResponse
import com.example.tripapplication.core.domain.util.NetworkError
import com.example.tripapplication.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Converts an HTTP response to a Result object.
 * Handles both successful responses and different types of errors.
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, AppError> {
    return try {
        when (response.status.value) {
            in 200..299 -> {
                try {
                    Result.Success(response.body<T>())
                } catch (e: NoTransformationFoundException) {
                    Log.e("API_ERROR", "Serialization error: ${e.message}")
                    Result.Error(AppError.Network(NetworkError.SERIALIZATION))
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Unknown error during serialization: ${e.message}")
                    Result.Error(AppError.Network(NetworkError.UNKNOWN))
                }
            }
            in 400..499 -> {
                try {
                    // Try to parse as BusinessError
                    val errorResponse = response.body<ErrorResponse>()
                    val businessError = BusinessError.fromCode(errorResponse.businessErrorCode)

                    // Check if it's a validation error with field errors
                    if (!errorResponse.errors.isNullOrEmpty()) {
                        Result.Error(AppError.Validation(
                            fieldErrors = errorResponse.errors,
                            error = businessError
                        ))
                    } else {
                        Result.Error(AppError.Business(
                            error = businessError,
                            message = errorResponse.businessErrorDescription
                        ))
                    }
                } catch (e: Exception) {
                    // If we can't parse the error response, fall back to generic errors
                    when (response.status.value) {
                        408 -> Result.Error(AppError.Network(NetworkError.REQUEST_TIMEOUT))
                        429 -> Result.Error(AppError.Network(NetworkError.TOO_MANY_REQUESTS))
                        else -> Result.Error(AppError.Network(NetworkError.UNKNOWN))
                    }
                }
            }
            in 500..599 -> Result.Error(AppError.Network(NetworkError.SERVER_ERROR))
            else -> Result.Error(AppError.Network(NetworkError.UNKNOWN))
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Unexpected error: ${e.message}")
        Result.Error(AppError.Network(NetworkError.UNKNOWN))
    }
}