package com.example.tripapplication.auth.domain

import com.example.tripapplication.auth.domain.utils.LoginRequest
import com.example.tripapplication.core.domain.util.AppError
import com.example.tripapplication.core.domain.util.NetworkError
import com.example.tripapplication.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthService {
    suspend fun getAuthState(): Flow<AuthResponse>
    suspend fun saveAuthState(authResponse: AuthResponse)
    suspend fun clearAuthState()

    suspend fun register(request: RegisterRequest): Result<Unit, AppError>
    suspend fun login(request: LoginRequest): Result<AuthResponse, AppError>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse, AppError>
    suspend fun activateAccount(token: String, email: String): Result<Unit, AppError>
    suspend fun resendCode(email: String): Result<Unit, AppError>
    suspend fun forgetPassword(email: String): Result<Unit, AppError>

}