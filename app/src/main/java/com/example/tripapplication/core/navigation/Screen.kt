package com.example.tripapplication.core.navigation

sealed class Screen(
    val route: String
) {
    data object RegisterScreen : Screen("register_screen")
    data object LoginScreen : Screen("login_screen")
    data object OnBoardingScreen : Screen("onboarding_screen")
    data object ForgetPasswordScreen : Screen("forget_password_screen")
    data object ActivateAccountScreen : Screen("activate_account_screen")
    data object HomeScreen : Screen("home_screen")
    data object CategoryScreen : Screen("category_screen")
    data object ProductDetailScreen : Screen("product_detail_screen")
    data object CartScreen : Screen("cart_screen")
    data object ProfileScreen : Screen("profile_screen")
}