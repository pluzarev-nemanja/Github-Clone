package com.example.data.model

import com.google.gson.annotations.SerializedName

data class FollowerResponse(
    @SerializedName("login")
    val name: String,
)
