package com.example.tripapplication.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripapplication.R
import com.example.tripapplication.auth.presentation.components.AgreementCheckbox
import com.example.tripapplication.auth.presentation.components.InputField
import com.example.tripapplication.auth.presentation.components.LogoComponent
import com.example.tripapplication.auth.presentation.components.PrimaryButton
import com.example.tripapplication.auth.presentation.components.SocialSignInButton
import com.example.tripapplication.auth.presentation.utils.isValidEmail
import com.example.tripapplication.auth.presentation.utils.isValidPassword

@Composable
fun LoginScreen() {
    val black = Color.Black
    val lightGray = Color(0xFFF7F7F7)
    var check by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Logo
            LogoComponent()
            
            Spacer(modifier = Modifier.height(32.dp))
            
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
                label = "Email Address",
                errorText = "Invalid email address",
                validator = ::isValidEmail,
                placeholder = "Enter your email address",
                isPassword = false
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Password field
            InputField(
                label = "Password",
                errorText = "Password must be at least 6 characters long",
                validator = ::isValidPassword,
                placeholder = "Enter your password",
                isPassword = !passwordVisible,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = if (passwordVisible) Color.Blue.copy(alpha = 0.6f) else Color.Gray
                        )
                    }
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
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Gray,
                    textDecoration = TextDecoration.None
                )
            }
            
            // User agreement checkbox
            AgreementCheckbox(
                checked = check,
                onCheckedChange = {
                    check = it
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sign in button
            PrimaryButton(
                text = "Sign in",
                enabled = check,
                onClick = { /* Handle sign in */ }
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
                    .clickable {  },
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


