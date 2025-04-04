package com.example.tripapplication.auth.presentation.activate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tripapplication.auth.presentation.activate.components.OtpInputField
import com.example.tripapplication.auth.presentation.activate.components.ResendOtpTimer
import com.example.tripapplication.auth.presentation.components.LogoComponent
import com.example.tripapplication.auth.presentation.login.AuthAction
import com.example.tripapplication.auth.presentation.login.AuthState

@Composable
fun ActivateScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    focusRequesters: List<FocusRequester>,
    modifier: Modifier = Modifier
) {
    val black = Color.Black


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
            LogoComponent()
            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "Enter OTP",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = black
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "An 4 digit OTP has been sent to your email",
                style = MaterialTheme.typography.bodyMedium,
                color = black
            )
            Spacer(modifier = Modifier.height(4.dp))
            state.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = black
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                state.code.forEachIndexed { index, number ->
                    OtpInputField(
                        number = number,
                        focusRequester = focusRequesters[index],
                        onFocusChanged = { isFocused ->
                            if(isFocused) {
                                onAction(AuthAction.OnChangeFieldFocused(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onAction(AuthAction.OnEnterNumber(newNumber, index))
                        },
                        onKeyboardBack = {
                            onAction(AuthAction.OnKeyboardBack)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            ResendOtpTimer {
                onAction(AuthAction.OnReSendCode)
            }
        }
    }
}


