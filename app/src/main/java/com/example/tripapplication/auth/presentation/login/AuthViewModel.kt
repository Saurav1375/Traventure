package com.example.tripapplication.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripapplication.auth.domain.AuthService
import com.example.tripapplication.auth.domain.RegisterRequest
import com.example.tripapplication.auth.domain.utils.LoginRequest
import com.example.tripapplication.core.domain.util.onError
import com.example.tripapplication.core.domain.util.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _register = MutableStateFlow(RegisterRequest("", "", "", ""))
    private val _login = MutableStateFlow(LoginRequest("", ""))

    private val _state = MutableStateFlow(AuthState())
    val state = combine(
        _state,
        _login,
        _register
    ) { state, login, register ->
        state.copy(
            loginRequest = login,
            registerRequest = register
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadAuthState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthState()
        )

    private val _event = Channel<AuthEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.Login -> login()
            is AuthAction.Register -> register()
            is AuthAction.Logout -> TODO()
            is AuthAction.OnLoginEmailChange -> _login.update { it.copy(email = action.email) }
            is AuthAction.OnLoginPasswordChange -> _login.update { it.copy(password = action.password) }
            is AuthAction.OnRegisterEmailChange -> _register.update { it.copy(email = action.email) }
            is AuthAction.OnRegisterNameChange -> _register.update { it.copy(firstname = action.firstname) }
            is AuthAction.OnRegisterPasswordChange -> _register.update { it.copy(password = action.password) }
            AuthAction.OnForgotPassword -> forgetPassword()
            AuthAction.OnReSendCode -> resendCode()

            is AuthAction.OnEnterNumber -> enterNumber(action.number, action.index)
            is AuthAction.OnChangeFieldFocused -> _state.update { it.copy(focusedIndex = action.index) }
            is AuthAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) {
                                null
                            } else {
                                number
                            }
                        },
                        focusedIndex = previousIndex
                    )
                }
            }


            else -> TODO("Handle actions")
        }
    }

    private fun loadAuthState() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            authService.getAuthState().collect { authState ->
                _state.update {
                    it.copy(
                        accessToken = authState.accessToken,
                        refreshToken = authState.refreshToken,
                        email = authState.email,
                        isActivated = authState.isActivated,
                        isLoading = false
                    )
                }

            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authService.login(_login.value)
                .onSuccess { authResponse ->
                    _event.send(AuthEvent.LoginSuccess)
                }.onError { error ->
                    _event.send(AuthEvent.Error(error.name))
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authService.register(_register.value)
                .onSuccess {
                    _event.send(AuthEvent.RegisterSuccess)
                }
                .onError { error ->
                    _event.send(AuthEvent.Error(error.name))
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun activateAccount(code: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authService.activateAccount(code)
                .onSuccess {
                    _event.send(AuthEvent.ActivationSuccess)
                }
                .onError { error ->
                    _event.send(AuthEvent.Error(error.name))
                }
            _state.update { it.copy(isLoading = false) }
        }


    }

    private fun resendCode() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.value.email?.let {
                authService.resendCode(it)
                    .onSuccess {
                        _event.send(AuthEvent.ResendCodeSuccess)
                    }
                    .onError { error ->
                        _event.send(AuthEvent.Error(error.name))
                    }
            }
            _state.update { it.copy(isLoading = false) }

        }

    }

    private fun forgetPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
                authService.forgetPassword(_login.value.email).onSuccess {
                    _event.send(AuthEvent.ForgetPassSuccess)
                }.onError { error ->
                    _event.send(AuthEvent.Error(error.name))
                }


        }
    }


    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update {
            it.copy(
                code = newCode,
                focusedIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
            )
        }

        if (newCode.none { it == null }) {
            activateAccount(newCode.joinToString(""))

        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }

        if (currentFocusedIndex == 3) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}