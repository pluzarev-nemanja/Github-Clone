package com.example.data.model

data class RepositoryDetailsResponse(
    val name: String,
    val owner: OwnerResponse,
    val description: String?,
    val forks: Int,
    val watchers: Int,
    val topics: List<String>,
)
