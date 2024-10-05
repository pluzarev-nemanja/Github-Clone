package com.example.data.model

import com.google.gson.annotations.SerializedName

data class BaseRepositoryResponse(
    @SerializedName("repo")
    val repo: RepositoryResponse,
)
