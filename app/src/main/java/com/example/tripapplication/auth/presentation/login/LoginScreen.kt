package com.example.tripapplication.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tripapplication.R
import com.example.tripapplication.auth.presentation.components.InputField
import com.example.tripapplication.auth.presentation.components.LogoComponent
import com.example.tripapplication.auth.presentation.components.PrimaryButton
import com.example.tripapplication.auth.presentation.components.SocialSignInButton
import com.example.tripapplication.auth.presentation.utils.isValidEmail
import com.example.tripapplication.auth.presentation.utils.isValidPassword

@Composable
fun LoginScreen(
    state: AuthState,
    navigateToRegister: () -> Unit,
    navigateToForgetPass : () -> Unit,
    onAction: (AuthAction) -> Unit
) {
    val black = Color.Black
    var passwordVisible by remember { mutableStateOf(false) }
    val isEnabled = isValidEmail(state.loginRequest.email) &&
                    isValidPassword(state.loginRequest.password)



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

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "Sign in to your account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            InputField(
                text = state.loginRequest.email,
                label = "Email Address",
                errorText = "Invalid email address",
                validator = ::isValidEmail,
                placeholder = "Enter your email address",
                isPassword = false,
                onValueChange = {
                    onAction(AuthAction.OnLoginEmailChange(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            InputField(
                text = state.loginRequest.password,
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
                    onAction(AuthAction.OnLoginPasswordChange(it))
                }
            )

            // Forgot password
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    modifier = Modifier.clickable(
                        indication = null, // disables ripple
                        interactionSource = remember { MutableInteractionSource() } ,
                        onClick = { navigateToForgetPass() }
                    ) ,
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Gray,
                    textDecoration = TextDecoration.None
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign in button
            PrimaryButton(
                text = "Sign in",
                enabled = isEnabled,
                onClick = { onAction(AuthAction.Login) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Alternative sign-in options
            Text(
                text = "other way to sign in",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Social sign-in options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialSignInButton(
                    icon = R.drawable._icon__google_,
                    contentDescription = "Sign in with Google"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .clickable(
                        indication = null, // disables ripple
                        interactionSource = remember { MutableInteractionSource() } ,
                    ) {
                        navigateToRegister()
                    },
                text = buildAnnotatedString {
                    append("New to us? ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        )

                    ) {
                        append("Register")
                    }
                },
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


