package com.example.tripapplication.auth.presentation.auth_screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onLoginSuccess()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val response = AuthorizationResponse.fromIntent(intent)
                val exception = AuthorizationException.fromIntent(intent)

                if (response != null) {
                    Log.d("LoginScreen", "Received auth response, processing... ${response.accessToken}")

                    viewModel.processAuthResponse(response)
                } else if (exception != null) {
                    Log.e("LoginScreen", "Auth error: ${exception.error} - ${exception.errorDescription}")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the App")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                launcher.launch(viewModel.loginIntent as Intent)
            }
        ) {
            Text("Login with Keycloak")
        }
    }
}

@Composable
fun AuthenticatedScreen(
    onLogout: () -> Unit,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    // Check token expiration and refresh if needed
    LaunchedEffect(Unit) {
        viewModel.refreshTokensIfNeeded()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("You are logged in!")
        Text("Access Token: ${authState.accessToken}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            }
        ) {
            Text("Logout")
        }
    }
}