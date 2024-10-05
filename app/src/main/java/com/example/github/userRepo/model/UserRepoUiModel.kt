package com.example.github.userRepo.model

data class UserRepoUiModel(
    val id: Int,
    val repositoryName: String,
    val userName: String,
    val userIcon: String,
    val userFollowers: String,
    val repositoryDescription: String,
    val issueCount: String,
    val labelNames: List<String>,
)
