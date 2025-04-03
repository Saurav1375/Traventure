package com.example.tripapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.tripapplication.auth.presentation.auth_screen.AuthViewModel
import com.example.tripapplication.auth.presentation.auth_screen.OnBoardingScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val viewModel = koinViewModel<AuthViewModel>()
            val authState by viewModel.authState.collectAsState()
            OnBoardingScreen()

//            NavHost(
//                navController = navController,
//                startDestination = if (authState.accessToken != null) "home" else "login"
//            ) {
//                composable("login") {
//                    LoginScreen(
//                        onLoginSuccess = {
//                            navController.navigate("home") {
//                                popUpTo("login") { inclusive = true }
//                            }
//                        },
//                        viewModel = viewModel
//                    )
//                }
//
//                composable("home") {
//                    AuthenticatedScreen(
//                        onLogout = {
//                            navController.navigate("login") {
//                                popUpTo("home") { inclusive = true }
//                            }
//                        },
//                        viewModel = viewModel
//                    )
//                }
//            }
        }
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        handleAuthorizationResponse(intent)
//    }
//
//    private fun handleAuthorizationResponse(intent: Intent) {
//        val response = AuthorizationResponse.fromIntent(intent)
//        val exception = AuthorizationException.fromIntent(intent)
//
//        if (response != null) {
//            Log.d("MainActivity", "Received auth response, processing...")
//            authViewModel.processAuthResponse(response)
//        } else if (exception != null) {
//            Log.e("MainActivity", "Auth error: ${exception.error} - ${exception.errorDescription}")
//        }
//    }
}