package com.example.github.userRepo.model

data class RepositoryDetailsUiModel(
    val repositoryName: String,
    val userName: String,
    val userIcon: String,
    val userFollowers: String,
    val repositoryDescription: String,
    val repositoryForks: String,
    val watchers: String,
    val topics: List<String>,
)
