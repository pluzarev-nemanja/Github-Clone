package com.example.data.model

import com.google.gson.annotations.SerializedName

data class RequestedReviewerResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("login")
    val name: String,
)
