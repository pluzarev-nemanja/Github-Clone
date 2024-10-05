package com.example.github.common.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.github.auth.screen.AuthScreen
import com.example.github.auth.uiState.LoginUiState
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.route.Graph
import com.example.github.common.navigation.route.LoginScreen

fun NavGraphBuilder.authNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginState: LoginUiState,
    userAction: (UserActionUiEvent) -> Unit,
) {
    navigation(route = Graph.AuthNavGraph.route, startDestination = LoginScreen.Login.route) {
        composable(LoginScreen.Login.route) {
            AuthScreen(
                navController = navController,
                userAction = userAction,
                loginState = loginState,
            )
        }
    }
}
