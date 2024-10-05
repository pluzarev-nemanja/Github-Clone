package com.example.github.common.navigation.route

sealed class Screen(val route: String) {
    data object Search : Screen(route = "searchScreen")

    data object RepositoryDetails : Screen(route = "repositoryDetailsScreen")

    data object PullRequestDetails : Screen(route = "pullRequestDetailsScreen")
}
