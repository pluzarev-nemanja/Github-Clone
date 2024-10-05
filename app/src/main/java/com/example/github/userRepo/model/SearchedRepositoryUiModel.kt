package com.example.github.userRepo.model

data class SearchedRepositoryUiModel(
    val repositoryName: String,
    val userName: String,
    val userIcon: String,
    val userFollowers: String,
    val repositoryDescription: String,
    val issuesUrl: String,
    val labelsUrl: String,
)
