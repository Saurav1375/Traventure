package com.example.tripapplication.auth.data.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import com.example.tripapplication.auth.data.local.AuthDataStore
import com.example.tripapplication.auth.data.local.KeycloakConfig
import com.example.tripapplication.auth.domain.AuthResponse
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.domain.utils.AuthError
import com.example.tripapplication.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import net.openid.appauth.browser.BrowserSelector
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthServiceImpl(
    private val authDataStore: AuthDataStore,
    private val keycloakConfig: KeycloakConfig,
    private val authService: AuthorizationService,
    private val context : Context
) : AuthService {

    override suspend fun getAuthState(): Flow<AuthResponse> = authDataStore.authState

    override suspend fun saveAuthState(authState: AuthResponse) =
        authDataStore.saveAuthState(authState)

    override suspend fun clearAuthState() = authDataStore.clearAuthState()

    override fun getLoginIntent(): Intent {
        val authRequest = keycloakConfig.getAuthRequest()

        val customTabIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .build()
        return authService.getAuthorizationRequestIntent(
            authRequest,
            customTabIntent,
        )
    }


    override suspend fun processAuthResponse(response: Any): Result<AuthResponse, AuthError> {
        return try {
            if (response !is AuthorizationResponse) {
                return Result.Error(AuthError.UNKNOWN)
            }

            val tokenResponse = performTokenRequestSuspend(
                response.createTokenExchangeRequest(),
                keycloakConfig.clientAuth
            )
            Log.d("LoginScreen", "Token response received: ${tokenResponse.accessToken}")

            val newAuthState = AuthResponse(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken,
                idToken = tokenResponse.idToken,
                accessTokenExpirationTime = System.currentTimeMillis() +
                        (tokenResponse.accessTokenExpirationTime ?: 0)
            )

            saveAuthState(newAuthState)
            Result.Success(newAuthState)
        } catch (e: Exception) {
            Log.e("AuthService", "Error processing auth response", e)
            Result.Error(AuthError.AUTH_FAILED)
        }
    }

    override suspend fun logout() {
        getAuthState().first().idToken?.let { idToken ->
            val endSessionEndpoint = keycloakConfig.serviceConfig.endSessionEndpoint
            if (endSessionEndpoint != null) {
                // Create end session request
                val endSessionRequest = net.openid.appauth.EndSessionRequest.Builder(keycloakConfig.serviceConfig)
                    .setIdTokenHint(idToken)
                    .setPostLogoutRedirectUri(Uri.parse("com.example.tripapplication://oauth2redirect"))
                    .build()

                // Get intent for ending the session
                val endSessionIntent = authService.getEndSessionRequestIntent(endSessionRequest)

                // Launch the intent (this needs to be handled by the Activity)
                // Since this is a service class, we return the intent to be launched by the caller
                clearAuthState()
                endSessionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(endSessionIntent)
            } else {
                // No end session endpoint, just clear state
                clearAuthState()
            }
        } ?: run {
            // No ID token, just clear state
            clearAuthState()
        }
    }



    override suspend fun refreshTokens(): Result<AuthResponse, AuthError> {
        return try {
            val currentState = authDataStore.authState.first()
            val refreshToken =
                currentState.refreshToken ?: return Result.Error(AuthError.AUTH_FAILED)

            if (currentState.accessTokenExpirationTime != null &&
                currentState.accessTokenExpirationTime > System.currentTimeMillis() + 60_000
            ) {
                // Token is still valid for more than a minute, no need to refresh
                return Result.Success(currentState)
            }

            val tokenRequest = TokenRequest.Builder(
                keycloakConfig.serviceConfig,
                keycloakConfig.clientId
            )
                .setGrantType("refresh_token")
                .setRefreshToken(refreshToken)
                .build()

            val tokenResponse = performTokenRequestSuspend(
                tokenRequest,
                keycloakConfig.clientAuth
            )

            val newAuthState = AuthResponse(
                accessToken = tokenResponse.accessToken,
                refreshToken = tokenResponse.refreshToken ?: currentState.refreshToken,
                idToken = tokenResponse.idToken,
                accessTokenExpirationTime = System.currentTimeMillis() + (tokenResponse.accessTokenExpirationTime
                    ?: 0)
            )

            saveAuthState(newAuthState)
            Log.d("LoginScreen", "New auth state saved: ${newAuthState.accessToken}")
            Result.Success(newAuthState)
        } catch (e: Exception) {
            Log.e("LoginScreen", "Error refreshing token", e)
            Result.Error(AuthError.TOKEN_REFRESH_FAILED)
        }
    }

    private suspend fun performTokenRequestSuspend(
        request: TokenRequest,
        clientAuth: ClientAuthentication
    ): TokenResponse = suspendCoroutine { continuation ->
        authService.performTokenRequest(request, clientAuth) { response, exception ->
            if (response != null) {
                continuation.resume(response)
            } else {
                continuation.resumeWithException(
                    exception ?: Exception("Unknown error during token request")
                )
            }
        }
    }
}