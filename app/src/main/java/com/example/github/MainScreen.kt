package com.example.github

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.NavBar
import com.example.github.common.navigation.graph.MainNavGraph
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.uiState.PullRequestUiState
import com.example.github.topic.uiState.TopicsUiState
import com.example.github.user.uiState.UserUiState
import com.example.github.userRepo.uiState.CommitUiState
import com.example.github.userRepo.uiState.RepositoryDetailsUiState
import com.example.github.userRepo.uiState.RepositoryUiState
import com.example.github.userRepo.uiState.SearchedRepositoryUiState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    userRepositoryUiState: RepositoryUiState,
    repositoryDetailsUiState: RepositoryDetailsUiState,
    topicsUiState: TopicsUiState,
    searchText: String,
    commitUiState: CommitUiState,
    searchRepositoryText: String,
    searchedRepositoryUiState: SearchedRepositoryUiState,
    isUserLoggedIn: Boolean,
    userUiState: UserUiState,
    pullRequestUiState: PullRequestUiState,
    pullRequestDetailsUiState: PullRequestDetailsUiState,
    userAction: (UserActionUiEvent) -> Unit,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavBar(
                navController = navController,
                userUiState = userUiState,
                isUserLoggedIn = isUserLoggedIn,
            )
        },
    ) { paddingValues ->
        MainNavGraph(
            navController = navController,
            paddingValues = paddingValues,
            userRepositoryUiState = userRepositoryUiState,
            pullRequestUiState = pullRequestUiState,
            topicsUiState = topicsUiState,
            searchedRepositoryUiState = searchedRepositoryUiState,
            repositoryDetailsUiState = repositoryDetailsUiState,
            pullRequestDetailsUiState = pullRequestDetailsUiState,
            searchText = searchText,
            userAction = userAction,
            commitUiState = commitUiState,
            searchRepositoryText = searchRepositoryText,
            isUserLoggedIn = isUserLoggedIn,
            userUiState = userUiState,
        )
    }
}
