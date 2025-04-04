package com.example.tripapplication.auth.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripapplication.R
import com.example.tripapplication.auth.presentation.onboarding.components.AutoAdvancePager
import com.example.tripapplication.auth.presentation.onboarding.components.GetStartedButton

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Add padding to the top of the logo to ensure proper spacing
        Image(
            modifier = Modifier
                .size(140.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "APP LOGO"
        )

        // The AutoAdvancePager should take up most of the available space
        AutoAdvancePager(
            pageItems = listOf(
                R.drawable.onboarding1,
                R.drawable.onboarding2,
                R.drawable.onboarding3
            ),
            modifier = Modifier.weight(1f, fill = true) // Give it weight to fill available space
        )

        // Add some space before the button
//        Spacer(modifier = Modifier.height(16.dp))

        // The button at the bottom with some padding
        GetStartedButton(
            onClick = { navigateToLogin() },
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnBoardingScreenPreview() {
    OnBoardingScreen() {}

}