package com.example.github.pullRequest.model

data class PullRequestDetailsUiModel(
    val authorName: String,
    val authorImage: String,
    val title: String,
    val description: String,
    val labels: List<LabelUiModel>,
    val reviewers: List<ReviewerUiModel>,
    val milestone: String,
)
