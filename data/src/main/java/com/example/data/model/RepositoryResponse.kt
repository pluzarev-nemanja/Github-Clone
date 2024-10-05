package com.example.data.model

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    val id: Int,
    val name: String,
    val owner: OwnerResponse,
    val description: String?,
    @SerializedName("issues_url")
    val issuesUrl: String,
    @SerializedName("labels_url")
    val labelsUrl: String,
    @SerializedName("open_issues_count")
    val openIssueCount: Int? = null,
)
