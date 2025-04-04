package com.example.tripapplication.core.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripapplication.auth.presentation.activate.ActivateScreen
import com.example.tripapplication.auth.presentation.forgetpass.ForgetPasswordScreen
import com.example.tripapplication.auth.presentation.forgetpass.components.PasswordRecoveryDialog
import com.example.tripapplication.auth.presentation.login.AuthAction
import com.example.tripapplication.auth.presentation.login.AuthEvent
import com.example.tripapplication.auth.presentation.login.AuthViewModel
import com.example.tripapplication.auth.presentation.login.LoginScreen
import com.example.tripapplication.auth.presentation.onboarding.OnBoardingScreen
import com.example.tripapplication.auth.presentation.register.RegisterScreen
import com.example.tripapplication.core.presentation.components.LoadingOverlay
import com.example.tripapplication.core.presentation.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authViewModel = koinViewModel<AuthViewModel>()
    val authState = authViewModel.state.collectAsStateWithLifecycle()
    var showPasswordResetDialogue by remember { mutableStateOf(false) }
    LaunchedEffect(authState.value.email) {
        if (authState.value.email != null && !authState.value.isActivated) {
            navController.navigate(Screen.ActivateAccountScreen.route) {
                popUpTo(Screen.OnBoardingScreen.route) {
                    inclusive = true
                }
            }
        }
    }
    ObserveAsEvents(events = authViewModel.event) { event ->
        when (event) {
            is AuthEvent.Error -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            AuthEvent.LoginSuccess -> {
                Toast.makeText(
                    context,
                    "Login Success",
                    Toast.LENGTH_LONG
                ).show()
            }
            AuthEvent.RegisterSuccess -> {
                Toast.makeText(
                    context,
                    "Register Success",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Screen.ActivateAccountScreen.route) {
                    popUpTo(Screen.RegisterScreen.route) {
                        inclusive = true
                    }
                }
            }
            AuthEvent.ActivationSuccess -> {
                Toast.makeText(
                    context,
                    "Activation Success",
                    Toast.LENGTH_LONG
                ).show()
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
                Toast.makeText(
                    context,
                    "Code Sent",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    LoadingOverlay(isLoading = authState.value.isLoading){
        NavHost(
            navController = navController,
            startDestination = Screen.OnBoardingScreen.route,
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
                    PasswordRecoveryDialog(
                        onDismissRequest = {
                            showPasswordResetDialogue = false
                        }
                    )


                }
                ForgetPasswordScreen(
                    state = authState.value,
                    onAction = {
                        authViewModel.onAction(it)
                    }
                )
            }
        }
    }


}