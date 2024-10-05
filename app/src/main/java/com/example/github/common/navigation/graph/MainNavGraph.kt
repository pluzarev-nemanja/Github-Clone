package com.example.github.common.navigation.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.github.common.action.UserActionUiEvent
import com.example.github.common.navigation.route.BottomNavItem
import com.example.github.common.navigation.route.Graph
import com.example.github.common.navigation.route.Screen
import com.example.github.pullRequest.screen.PullRequestDetailsScreen
import com.example.github.pullRequest.screen.PullRequestScreen
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.uiState.PullRequestUiState
import com.example.github.topic.screen.SearchTopicScreen
import com.example.github.topic.uiState.TopicsUiState
import com.example.github.user.screen.UserScreen
import com.example.github.user.uiState.UserUiState
import com.example.github.userRepo.screen.RepositoryDetailsScreen
import com.example.github.userRepo.screen.SearchScreen
import com.example.github.userRepo.screen.UserRepositoryScreen
import com.example.github.userRepo.uiState.CommitUiState
import com.example.github.userRepo.uiState.RepositoryDetailsUiState
import com.example.github.userRepo.uiState.RepositoryUiState
import com.example.github.userRepo.uiState.SearchedRepositoryUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    userRepositoryUiState: RepositoryUiState,
    pullRequestUiState: PullRequestUiState,
    topicsUiState: TopicsUiState,
    searchRepositoryText: String,
    commitUiState: CommitUiState,
    searchedRepositoryUiState: SearchedRepositoryUiState,
    pullRequestDetailsUiState: PullRequestDetailsUiState,
    repositoryDetailsUiState: RepositoryDetailsUiState,
    searchText: String,
    isUserLoggedIn: Boolean,
    userUiState: UserUiState,
    userAction: (UserActionUiEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.UserRepository.route,
        route = Graph.MainNavGraph.route,
    ) {
        composable(BottomNavItem.UserRepository.route) {
            UserRepositoryScreen(
                navController = navController,
                userRepositoryUiState = userRepositoryUiState,
                bottomPadding = paddingValues,
                userAction = userAction,
            )
        }
        composable(BottomNavItem.PullRequest.route) {
            PullRequestScreen(
                pullRequestUiState = pullRequestUiState,
                bottomPaddingValues = paddingValues,
                navController = navController,
                userAction = userAction,
            )
        }
        composable(BottomNavItem.SearchTopic.route) {
            SearchTopicScreen(
                bottomPadding = paddingValues,
                topicsUiState = topicsUiState,
                searchText = searchText,
                userAction = userAction,
            )
        }
        composable(BottomNavItem.User.route) {
            UserScreen(
                userUiState = userUiState,
                bottomPadding = paddingValues,
                navController = navController,
                userAction = userAction,
                isUserLoggedIn = isUserLoggedIn,
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                searchedRepositoryUiState = searchedRepositoryUiState,
                bottomPadding = paddingValues,
                userAction = userAction,
                navController = navController,
                searchRepositoryText = searchRepositoryText,
            )
        }
        composable(Screen.RepositoryDetails.route) {
            RepositoryDetailsScreen(
                repositoryDetailsUiState = repositoryDetailsUiState,
                paddingValues = paddingValues,
                commitUiState = commitUiState,
                navController = navController,
                userAction = userAction,
            )
        }
        composable(Screen.PullRequestDetails.route) {
            PullRequestDetailsScreen(
                pullRequestDetailsUiState = pullRequestDetailsUiState,
                navController = navController,
                bottomPaddingValues = paddingValues,
                userAction = userAction,
            )
        }
    }
}
