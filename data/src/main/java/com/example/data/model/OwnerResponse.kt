package com.example.data.model

import com.google.gson.annotations.SerializedName

data class OwnerResponse(
    @SerializedName("login")
    val authorName: String,
    @SerializedName("avatar_url")
    val authorIcon: String,
    @SerializedName("followers_url")
    val followers: String,
)
