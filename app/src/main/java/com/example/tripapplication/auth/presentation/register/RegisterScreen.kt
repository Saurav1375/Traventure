package com.example.tripapplication.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tripapplication.auth.presentation.components.AgreementCheckbox
import com.example.tripapplication.auth.presentation.components.InputField
import com.example.tripapplication.auth.presentation.components.LogoComponent
import com.example.tripapplication.auth.presentation.components.PrimaryButton
import com.example.tripapplication.auth.presentation.login.AuthAction
import com.example.tripapplication.auth.presentation.login.AuthState
import com.example.tripapplication.auth.presentation.utils.isValidEmail
import com.example.tripapplication.auth.presentation.utils.isValidPassword

@Composable
fun RegisterScreen(
    state: AuthState,
    navigateToLogin: () -> Unit,
    onAction: (AuthAction) -> Unit
) {
    val black = Color.Black
    var checkPrivacyPolicy by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val isEnabled = checkPrivacyPolicy &&
                    state.registerRequest.firstname.isNotEmpty() &&
                    isValidEmail(state.registerRequest.email) &&
                    isValidPassword(state.registerRequest.password)



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            LogoComponent()

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = "Create new account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = black
            )

            Spacer(modifier = Modifier.height(32.dp))

            InputField(
                text = state.registerRequest.firstname,
                label = "Name",
                errorText = "Can not be empty",
                validator = {
                    it.isNotEmpty()
                },
                placeholder = "Enter your name",
                isPassword = false,
                onValueChange = {
                    onAction(AuthAction.OnRegisterNameChange(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            InputField(
                text = state.registerRequest.email,
                label = "Email Address",
                errorText = "Invalid email address",
                validator = ::isValidEmail,
                placeholder = "Enter your email address",
                isPassword = false,
                onValueChange = {
                    onAction(AuthAction.OnRegisterEmailChange(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            InputField(
                text = state.registerRequest.password,
                label = "Password",
                errorText = "Password must be at least 8 characters long",
                validator = ::isValidPassword,
                placeholder = "Enter your password",
                isPassword = !passwordVisible,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = if (passwordVisible) Color.Black else Color.Gray
                        )
                    }
                },
                onValueChange = {
                    onAction(AuthAction.OnRegisterPasswordChange(it))
                }
            )

            // User agreement checkbox
            AgreementCheckbox(
                checked = checkPrivacyPolicy,
                onCheckedChange = {
                    checkPrivacyPolicy = it
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sign in button
            PrimaryButton(
                text = "Sign Up",
                enabled = isEnabled,
                onClick = { onAction(AuthAction.Register) }
            )


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .clickable {
                        navigateToLogin()
                    },
                text = buildAnnotatedString {
                    append("Already Registered? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        )

                    ) {
                        append("Login")
                    }
                },
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


