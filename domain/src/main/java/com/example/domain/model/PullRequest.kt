package com.example.domain.model

data class PullRequest(
    val id: Int,
    val repositoryName: String,
    val ownerName: String,
    val title: String,
    val userName: String,
    val userImage: String,
    val labels: List<Label>,
    val requestedReviewers: List<RequestedReviewer>,
)
