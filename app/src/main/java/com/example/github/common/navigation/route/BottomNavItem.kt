package com.example.github.common.navigation.route

import androidx.annotation.DrawableRes
import com.example.github.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    val title: String,
) {
    data object UserRepository : BottomNavItem(route = "userRepositoryScreen", icon = R.drawable.ic_repository, title = "Repos")

    data object PullRequest : BottomNavItem(route = "pullRequestScreen", icon = R.drawable.ic_pull_request, title = "Pulls")

    data object SearchTopic : BottomNavItem(route = "searchTopicScreen", icon = R.drawable.ic_topic, title = "Topics")

    data object User : BottomNavItem(route = "userProfileScreen", icon = R.drawable.ic_user_profile, title = "Profile")
}
