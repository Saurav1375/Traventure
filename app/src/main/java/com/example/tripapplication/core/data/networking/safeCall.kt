package com.example.tripapplication.core.data.networking

import com.example.tripapplication.core.domain.util.AppError
import com.example.tripapplication.core.domain.util.NetworkError
import com.example.tripapplication.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, AppError> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return Result.Error(AppError.Network(NetworkError.NO_INTERNET))
    } catch(e: SerializationException) {
        return Result.Error(AppError.Network(NetworkError.SERIALIZATION))
    } catch(e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(AppError.Network(NetworkError.UNKNOWN))
    }

    return responseToResult(response)
}