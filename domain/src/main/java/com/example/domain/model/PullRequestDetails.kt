package com.example.domain.model

data class PullRequestDetails(
    val userName: String,
    val userImage: String,
    val description: String?,
    val title: String,
    val labels: List<Label>,
    val milestone: String?,
    val requestedReviewers: List<RequestedReviewer>,
)
