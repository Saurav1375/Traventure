package com.example.tripapplication.core.data.networking

import android.util.Log
import com.example.tripapplication.auth.data.local.AuthDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Auth) {
                bearer {
//                    loadTokens {
//                        val accessToken = authService.getAuthState().first().accessToken
//                        val refreshToken = authService.getAuthState().first().refreshToken
//                        BearerTokens(accessToken ?: "", refreshToken ?: "")
//                    }

//                    refreshTokens {
//                        try {
//                            authService.refreshTokens()
//                            BearerTokens(
//                                accessToken = authService.getAuthState().first().accessToken ?: "",
//                                refreshToken = authService.getAuthState().first().refreshToken ?: ""
//                            )
//
//
//                        } catch (e: Exception) {
//                            Log.d("TAG", "Login: ${e.message}")
//                            //todo() redirect to logout
//                            null
//                        }
//
//                    }
                }

            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

        }
    }
}