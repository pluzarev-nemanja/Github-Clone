package com.example.data.model

data class SearchedRepositoryWithIssue(
    val repositories: List<RepositoryResponse>,
    val openIssuesTotalCount: List<Int>,
    val labels: List<LabelResponse>,
)
