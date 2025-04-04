package com.example.tripapplication.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main content - will be blurred when loading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (isLoading) 5.dp else 0.dp)
        ) {
            content()
        }
        
        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                // Lottie Animation
                DotLottieAnimation(
                    source = DotLottieSource.Url("https://lottie.host/85e9bccd-993b-4a8d-b330-425cf598aed5/gD9awEaM1E.lottie"),
                    autoplay = true,
                    loop = true,
                    speed =  1.8f,
                    useFrameInterpolation = false,
                    playMode = Mode.FORWARD,
                    modifier = Modifier.background(Color.Transparent)
                )
            }
        }
    }
}

