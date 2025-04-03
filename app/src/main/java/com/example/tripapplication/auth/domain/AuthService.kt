package com.example.tripapplication.auth.domain

import com.example.tripapplication.auth.domain.utils.AuthError
import com.example.tripapplication.core.domain.util.Result
import kotlinx.coroutines.flow.Flow


interface AuthService {
    suspend fun getAuthState(): Flow<AuthResponse>
    suspend fun saveAuthState(authState: AuthResponse)
    suspend fun clearAuthState()
    suspend fun refreshTokens(): Result<AuthResponse, AuthError>
    fun getLoginIntent(): Any
    suspend fun processAuthResponse(response: Any): Result<AuthResponse, AuthError>
    suspend fun logout()
}