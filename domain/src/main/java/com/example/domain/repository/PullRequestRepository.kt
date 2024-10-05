package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.PullRequest
import com.example.domain.model.PullRequestDetails
import kotlinx.coroutines.flow.Flow

interface PullRequestRepository {
    suspend fun getPullRequests(
        owner: String,
        repository: String,
    ): Flow<PagingData<PullRequest>>

    suspend fun getPullRequestDetails(
        owner: String,
        repository: String,
        pullNumber: Int,
    ): PullRequestDetails
}
