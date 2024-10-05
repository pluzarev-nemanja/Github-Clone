package com.example.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val name: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val email: String?,
    val company: String?,
    @SerializedName("bio")
    val biography: String?,
    val followers: Int,
    val following: Int,
    val location: String?,
)
