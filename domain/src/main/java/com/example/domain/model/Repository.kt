package com.example.domain.model

data class Repository(
    val id: Int,
    val name: String,
    val authorName: String,
    val authorIcon: String,
    val followers: String,
    val description: String?,
    val issuesCount: Int,
    val labelNames: List<String>,
)
