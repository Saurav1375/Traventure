package com.example.tripapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tripapplication.auth.presentation.login.LoginScreen
import com.example.tripapplication.core.navigation.NavigationGraph

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val navController = rememberNavController()
                NavigationGraph(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)

                )
            }


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

}