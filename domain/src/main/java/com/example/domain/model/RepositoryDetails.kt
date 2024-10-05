package com.example.domain.model

data class RepositoryDetails(
    val name: String,
    val authorName: String,
    val authorIcon: String,
    val followers: String,
    val description: String?,
    val forks: Int,
    val watchers: Int,
    val topics: List<String>,
)
