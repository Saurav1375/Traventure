package com.example.tripapplication.auth.presentation.activate.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripapplication.auth.presentation.utils.formatTimeForResendTImer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResendOtpTimer(
    initialTimerSeconds: Int = 30,
    onResendClick: () -> Unit
) {
    var timeRemaining by remember { mutableStateOf(initialTimerSeconds) }
    var canResend by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        while (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        }
        canResend = true
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
                if (canResend) {
                    // Reset timer
                    timeRemaining = initialTimerSeconds
                    canResend = false
                    onResendClick()
                    scope.launch {
                        while (timeRemaining > 0) {
                            delay(1000)
                            timeRemaining--
                        }
                        canResend = true
                    }

                }
            },
            enabled = canResend,
            modifier = Modifier.padding(end = 2.dp)
        ) {
            Text(
                text = "Resend OTP",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (canResend) Color.Black else Color.Gray
            )
        }

        if (!canResend) {
            Text(
                text = "(${formatTimeForResendTImer(timeRemaining)})",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResendOtpTimerPreview() {
    MaterialTheme {
        ResendOtpTimer(
            initialTimerSeconds = 12,
            onResendClick = {}
        )
    }
}