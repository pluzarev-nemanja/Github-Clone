package com.example.data.model

import com.google.gson.annotations.SerializedName

data class TopicResponse(
    @SerializedName("display_name")
    val name: String?,
    @SerializedName("short_description")
    val shortDescription: String?,
    @SerializedName("created_by")
    val createdBy: String?,
    @SerializedName("created_at")
    val createdAt: String?,
)
