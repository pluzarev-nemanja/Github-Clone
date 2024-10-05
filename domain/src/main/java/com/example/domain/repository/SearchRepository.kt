package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.SearchedRepository
import com.example.domain.model.Topic
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchRepositories(name: String): Flow<PagingData<SearchedRepository>>

    suspend fun searchTopics(name: String): Flow<PagingData<Topic>>
}
