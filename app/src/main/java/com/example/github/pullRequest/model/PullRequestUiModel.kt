package com.example.github.pullRequest.model

data class PullRequestUiModel(
    val id: Int,
    val repositoryName: String,
    val ownerName: String,
    val title: String,
    val userName: String,
    val userImage: String,
    val labels: List<LabelUiModel>,
    val reviewers: List<ReviewerUiModel>,
)
