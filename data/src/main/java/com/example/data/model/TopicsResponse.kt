package com.example.data.model

import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val topics: List<TopicResponse>,
    @SerializedName("total_count")
    val totalCount: Int,
)
