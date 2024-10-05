package com.example.data.model

data class RepositoryWithIssue(
    val repositories: List<RepositoryResponse>,
    val openIssuesTotalCount: List<Int>,
    val labels: List<LabelResponse>,
)
