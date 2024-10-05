package com.example.github.common.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.github.MainScreen
import com.example.github.auth.uiState.LoginUiState
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.route.Graph
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.uiState.PullRequestUiState
import com.example.github.topic.uiState.TopicsUiState
import com.example.github.user.uiState.UserUiState
import com.example.github.userRepo.uiState.CommitUiState
import com.example.github.userRepo.uiState.RepositoryDetailsUiState
import com.example.github.userRepo.uiState.RepositoryUiState
import com.example.github.userRepo.uiState.SearchedRepositoryUiState

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController,
    userRepositoryUiState: RepositoryUiState,
    repositoryDetailsUiState: RepositoryDetailsUiState,
    topicsUiState: TopicsUiState,
    searchText: String,
    commitUiState: CommitUiState,
    searchRepositoryText: String,
    searchedRepositoryUiState: SearchedRepositoryUiState,
    isUserLoggedIn: Boolean,
    userUiState: UserUiState,
    loginState: LoginUiState,
    pullRequestUiState: PullRequestUiState,
    pullRequestDetailsUiState: PullRequestDetailsUiState,
    userAction: (UserActionUiEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = Graph.RootNavGraph.route,
    ) {
        authNavGraph(
            navController = navController,
            loginState = loginState,
            userAction = userAction,
        )

        composable(Graph.MainNavGraph.route) {
            MainScreen(
                userRepositoryUiState = userRepositoryUiState,
                repositoryDetailsUiState = repositoryDetailsUiState,
                topicsUiState = topicsUiState,
                searchText = searchText,
                userUiState = userUiState,
                searchedRepositoryUiState = searchedRepositoryUiState,
                pullRequestUiState = pullRequestUiState,
                pullRequestDetailsUiState = pullRequestDetailsUiState,
                userAction = userAction,
                commitUiState = commitUiState,
                searchRepositoryText = searchRepositoryText,
                isUserLoggedIn = isUserLoggedIn,
            )
        }
    }
}
