package com.example.data.model

import com.google.gson.annotations.SerializedName

data class PullRequestDetailsResponse(
    val user: User,
    val title: String,
    val body: String?,
    val labels: List<LabelResponse>,
    val milestone: MilestoneResponse?,
    @SerializedName("requested_reviewers")
    val requestedReviewers: List<RequestedReviewerResponse>,
)
