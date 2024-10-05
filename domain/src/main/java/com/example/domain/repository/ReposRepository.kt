package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.Repository
import com.example.domain.model.RepositoryDetails
import kotlinx.coroutines.flow.Flow

interface ReposRepository {
    suspend fun getAllRepositories(): Flow<PagingData<Repository>>

    suspend fun getAuthRepositories(token: String): Flow<PagingData<Repository>>

    suspend fun getRepositoryDetails(
        owner: String,
        repository: String,
    ): RepositoryDetails

    suspend fun getRepositoryCommits(
        owner: String,
        repository: String,
    ): Flow<List<Int>>
}
