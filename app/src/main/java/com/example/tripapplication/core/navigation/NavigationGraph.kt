package com.example.tripapplication.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripapplication.auth.domain.utils.BusinessError
import com.example.tripapplication.auth.presentation.activate.ActivateScreen
import com.example.tripapplication.auth.presentation.forgetpass.ForgetPasswordScreen
import com.example.tripapplication.auth.presentation.forgetpass.components.CustomAlertDialog
import com.example.tripapplication.auth.presentation.login.AuthAction
import com.example.tripapplication.auth.presentation.login.AuthEvent
import com.example.tripapplication.auth.presentation.login.AuthViewModel
import com.example.tripapplication.auth.presentation.login.LoginScreen
import com.example.tripapplication.auth.presentation.onboarding.OnBoardingScreen
import com.example.tripapplication.auth.presentation.register.RegisterScreen
import com.example.tripapplication.core.domain.util.AppError
import com.example.tripapplication.core.presentation.components.LoadingOverlay
import com.example.tripapplication.core.presentation.util.ObserveAsEvents
import com.example.tripapplication.core.presentation.util.toUserMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val authViewModel = koinViewModel<AuthViewModel>()
    val authState = authViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showPasswordResetDialogue by remember { mutableStateOf(false) }
    var showAccountActivatedDialogue by  remember { mutableStateOf(false) }
    LaunchedEffect(authState.value.email) {
        if (authState.value.email != null && !authState.value.isActivated) {
            navController.navigate(Screen.ActivateAccountScreen.route) {
                popUpTo(Screen.OnBoardingScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    if (showAccountActivatedDialogue) {
        LaunchedEffect(Unit) {
            delay(8000)
            showAccountActivatedDialogue = false
        }
        CustomAlertDialog(
            onDismissRequest = { showAccountActivatedDialogue = false },
            title = "Account Activated",
            description = "Your Account is activated You can now login to your account",
            icon = Icons.Outlined.AccountCircle
        )
    }
    ObserveAsEvents(events = authViewModel.event) { event ->
        when (event) {
            is AuthEvent.Error -> {
                scope.launch {
                    snackBarHostState
                        .showSnackbar(
                            message =  event.error.toUserMessage(context),
                            duration = SnackbarDuration.Short
                        )
                }
                if (event.error is AppError.Business) {
                    if (event.error.error == BusinessError.ACCOUNT_NOT_VERIFIED) {
                        navController.navigate(Screen.ActivateAccountScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }

            AuthEvent.LoginSuccess -> {
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.LoginScreen.route) {
                        inclusive = true
                    }
                }
            }
            AuthEvent.RegisterSuccess -> {
                navController.navigate(Screen.ActivateAccountScreen.route) {
                    popUpTo(Screen.RegisterScreen.route) {
                        inclusive = true
                    }
                }
            }
            AuthEvent.ActivationSuccess -> {
                showAccountActivatedDialogue = true
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.ActivateAccountScreen.route) {
                        inclusive = true
                    }
                }
            }

            AuthEvent.ForgetPassSuccess -> {
                showPasswordResetDialogue = true
            }

            AuthEvent.ResendCodeSuccess -> {
                scope.launch {
                    snackBarHostState
                        .showSnackbar(
                            message =  "Activation code resent!",
                            duration = SnackbarDuration.Short
                        )
                }
            }
        }
    }
    LoadingOverlay(isLoading = authState.value.isLoading){
        NavHost(
            navController = navController,
            startDestination = if (authState.value.accessToken != null) Screen.HomeScreen.route else Screen.OnBoardingScreen.route,
            modifier = modifier
        ) {
            composable(route = Screen.OnBoardingScreen.route) {
                OnBoardingScreen(
                    navigateToLogin = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.OnBoardingScreen.route) {
                                inclusive = true
                            }
                        }

                    }
                )
            }


            composable(route = Screen.LoginScreen.route) {

                LoginScreen(
                    state = authState.value,
                    navigateToRegister = {
                        navController.navigate(Screen.RegisterScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToForgetPass = {
                        navController.navigate(Screen.ForgetPasswordScreen.route)
                    },
                    onAction = {
                        authViewModel.onAction(it)
                    }
                )
            }

            composable(Screen.RegisterScreen.route) {
                RegisterScreen(
                    state = authState.value,
                    navigateToLogin = {
                        navController.navigate(Screen.LoginScreen.route)
                    },
                    onAction = {
                        authViewModel.onAction(it)
                    }
                )

            }
            composable(Screen.ActivateAccountScreen.route) {
                val focusRequesters = remember {
                    List(4) { FocusRequester() }
                }
                val focusManager = LocalFocusManager.current
                val keyboardManager = LocalSoftwareKeyboardController.current

                LaunchedEffect(authState.value.focusedIndex) {
                    authState.value.focusedIndex?.let { index ->
                        focusRequesters.getOrNull(index)?.requestFocus()
                    }
                }

                LaunchedEffect(authState.value.code, keyboardManager) {
                    val allNumbersEntered = authState.value.code.none { it == null }
                    if (allNumbersEntered) {
                        focusRequesters.forEach {
                            it.freeFocus()
                        }
                        focusManager.clearFocus()
                        keyboardManager?.hide()
                    }
                }

                ActivateScreen(
                    state = authState.value,
                    focusRequesters = focusRequesters,
                    onAction = { action ->
                        when (action) {
                            is AuthAction.OnEnterNumber -> {
                                if (action.number != null) {
                                    focusRequesters[action.index].freeFocus()
                                }
                            }

                            else -> Unit
                        }
                        authViewModel.onAction(action)
                    },
                    modifier = modifier
                )
            }

            composable(Screen.ForgetPasswordScreen.route) {
                if (showPasswordResetDialogue) {
                    CustomAlertDialog(onDismissRequest = { showPasswordResetDialogue = false })
                }
                ForgetPasswordScreen(
                    state = authState.value,
                    onAction = {
                        authViewModel.onAction(it)
                    }
                )
            }

            composable(Screen.HomeScreen.route) {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(text = "Home Screen")
                    Text(authState.toString())
                }
            }
        }
    }


}