package com.example.tripapplication.auth.domain

import com.example.tripapplication.auth.domain.utils.LoginRequest
import com.example.tripapplication.core.domain.util.NetworkError
import com.example.tripapplication.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthService {
    suspend fun getAuthState(): Flow<AuthResponse>
    suspend fun saveAuthState(authResponse: AuthResponse)
    suspend fun clearAuthState()
    suspend fun register(request: RegisterRequest): Result<Unit, NetworkError>
    suspend fun login(request: LoginRequest): Result<AuthResponse, NetworkError>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse, NetworkError>
    suspend fun activateAccount(token: String): Result<Unit, NetworkError>
    suspend fun resendCode(email: String): Result<Unit, NetworkError>
    suspend fun forgetPassword(email: String): Result<Unit, NetworkError>

}