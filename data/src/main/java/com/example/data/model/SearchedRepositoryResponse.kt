package com.example.data.model

import com.google.gson.annotations.SerializedName

data class SearchedRepositoryResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repositories: List<RepositoryResponse>,
    @SerializedName("total_count")
    val totalCount: Int,
)
