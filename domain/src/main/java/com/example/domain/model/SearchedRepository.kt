package com.example.domain.model

data class SearchedRepository(
    val name: String,
    val authorName: String,
    val authorIcon: String,
    val authorFollowers: String,
    val description: String?,
    val issueCount: Int,
    val labelNames: List<String>,
)
