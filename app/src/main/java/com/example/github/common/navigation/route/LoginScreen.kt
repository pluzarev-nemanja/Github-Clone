package com.example.github.common.navigation.route

sealed class LoginScreen(val route: String) {
    data object Login : LoginScreen("loginScreen")
}
