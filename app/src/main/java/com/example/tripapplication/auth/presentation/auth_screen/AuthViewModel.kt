package com.example.tripapplication.auth.presentation.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.domain.utils.AuthError
import com.example.tripapplication.core.domain.util.onError
import com.example.tripapplication.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

class AuthViewModel(
    private val authService: AuthService,
    private val authorizationService: AuthorizationService
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.onStart {
        loadAuthState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthState()
    )

    private fun loadAuthState() {
        viewModelScope.launch {
            authService.getAuthState().collect { authState ->
                _authState.update {
                    it.copy(
                        isAuthenticated = false,
                        accessToken = authState.accessToken,
                        refreshToken = authState.refreshToken,
                        idToken = authState.idToken,
                        accessTokenExpirationTime = authState.accessTokenExpirationTime
                    )
                }
            }
        }

    }

    val loginIntent = authService.getLoginIntent()

    fun processAuthResponse(response: AuthorizationResponse) {
        viewModelScope.launch {
            authService.processAuthResponse(response)
                .onSuccess { authState ->
                    _authState.update {
                        it.copy(
                            isAuthenticated = true,
                        )
                    }

                }
                .onError { error ->
                    // Handle specific errors
                    when (error) {
                        AuthError.AUTH_FAILED -> {
                            // Handle auth failed error
                        }

                        AuthError.UNKNOWN -> {
                            // Handle unknown error
                        }

                        AuthError.TOKEN_EXCHANGE_FAILED -> TODO()
                        AuthError.NO_REFRESH_TOKEN -> TODO()
                        AuthError.DATASTORE_ERROR -> TODO()
                        AuthError.TOKEN_REFRESH_FAILED -> TODO()
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
        }
    }

    fun refreshTokensIfNeeded() {
        viewModelScope.launch {
            authService.refreshTokens()
        }
    }

    override fun onCleared() {
        authorizationService.dispose()
        super.onCleared()
    }
}