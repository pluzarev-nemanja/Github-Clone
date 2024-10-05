package com.example.github.common.navigation.route

sealed class Graph(val route: String) {
    data object MainNavGraph : Graph("mainNavGraph")

    data object AuthNavGraph : Graph("authNavGraph")

    data object RootNavGraph : Graph("rootNavGraph")
}
