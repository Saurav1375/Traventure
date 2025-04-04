package com.example.tripapplication.auth.data.networking

import com.example.tripapplication.auth.domain.RegisterRequest
import com.example.tripapplication.auth.data.local.AuthDataStore
import com.example.tripapplication.auth.data.mappers.toRegisterRequestDto
import com.example.tripapplication.auth.data.mappers.toAuthResponse
import com.example.tripapplication.auth.data.mappers.toLoginRequestDto
import com.example.tripapplication.auth.data.networking.dto.TokenResponseDto
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.domain.AuthResponse
import com.example.tripapplication.auth.domain.utils.LoginRequest
import com.example.tripapplication.core.data.networking.constructUrl
import com.example.tripapplication.core.data.networking.safeCall
import com.example.tripapplication.core.domain.util.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.example.tripapplication.core.domain.util.Result
import com.example.tripapplication.core.domain.util.map
import kotlinx.coroutines.flow.Flow

class AuthServiceImpl(
    private val httpClient: HttpClient,
    private val authDataStore: AuthDataStore
) : AuthService {
    override suspend fun getAuthState(): Flow<AuthResponse> = authDataStore.authState

    override suspend fun saveAuthState(authResponse: AuthResponse) =
        authDataStore.saveAuthState(authResponse)

    override suspend fun clearAuthState() = authDataStore.clearAuthState()
    override suspend fun register(request: RegisterRequest): Result<Unit, NetworkError> {
        val fullName = request.firstname.trimEnd()
        val formattedRequest = RegisterRequest(
            firstname = fullName.split(" ").first(),
            lastname = fullName.split(" ").last(),
            email = request.email,
            password = request.password
        )
        return safeCall<Unit> {
            httpClient.post(constructUrl("/auth/register")) {
                contentType(ContentType.Application.Json)
                setBody(formattedRequest.toRegisterRequestDto())
            }
        }.also {
            if (it is Result.Success) {
                saveAuthState(AuthResponse(request.email, false))
            }
        }
    }

    override suspend fun login(
        request: LoginRequest
    ): Result<AuthResponse, NetworkError> {
        return safeCall<TokenResponseDto> {
            httpClient.post(constructUrl("/auth/authenticate")) {
                contentType(ContentType.Application.Json)
                setBody(request.toLoginRequestDto())
            }
        }.map(TokenResponseDto::toAuthResponse).also {
            if (it is Result.Success) {
                saveAuthState(it.data)
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse, NetworkError> {
        return safeCall<TokenResponseDto> {
            httpClient.post(constructUrl("/auth/refresh-token")) {
                contentType(ContentType.Application.Json)
            }
        }.map(TokenResponseDto::toAuthResponse)
    }

    override suspend fun activateAccount(token: String): Result<Unit, NetworkError> {
        return safeCall<Unit> {
            httpClient.get(constructUrl("/auth/activate-account")) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("token", token)
                }
            }
        }.also {
            if (it is Result.Success) {
                saveAuthState(AuthResponse(null, true))
            }
        }
    }

    override suspend fun resendCode(email: String): Result<Unit, NetworkError> {
        return safeCall<Unit> {
            httpClient.get(constructUrl("/auth/resend-code")) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("email", email)
                }
            }
        }
    }

    override suspend fun forgetPassword(email: String): Result<Unit, NetworkError> {
        return safeCall<Unit> {
            httpClient.get(constructUrl("/auth/forget-password")) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("email", email)
                }
            }
        }
    }
}