package com.example.tripapplication.core.navigation

sealed class Screen(
    val route: String
) {
    data object RegisterScreen : Screen("register_screen")
    data object LoginScreen : Screen("login_screen")
    data object ActivationScreen : Screen("activation_screen")
    data object HomeScreen : Screen("home_screen")
    data object CategoryScreen : Screen("category_screen")
    data object ProductDetailScreen : Screen("product_detail_screen")
    data object CartScreen : Screen("cart_screen")
    data object ProfileScreen : Screen("profile_screen")
}