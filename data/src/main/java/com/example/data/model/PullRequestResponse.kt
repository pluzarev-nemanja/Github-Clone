package com.example.data.model

import com.google.gson.annotations.SerializedName

data class PullRequestResponse(
    @SerializedName("number")
    val id: Int,
    val title: String,
    val user: User,
    @SerializedName("base")
    val baseResponse: BaseRepositoryResponse,
    val labels: List<LabelResponse>,
    @SerializedName("requested_reviewers")
    val requestedReviewers: List<RequestedReviewerResponse>,
)
