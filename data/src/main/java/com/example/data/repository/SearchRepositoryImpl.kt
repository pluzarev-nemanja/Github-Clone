package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.mapper.SearchedRepositoryResponseToSearchedRepositoryMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.mapper.TopicResponseToTopicMapper
import com.example.data.pagingSource.SearchRepositoriesPagingSource
import com.example.data.pagingSource.SearchTopicsPagingSource
import com.example.data.remote.SearchApi
import com.example.data.util.Constants.PAGE_SIZE
import com.example.data.util.Constants.SORT_ORDER
import com.example.domain.model.SearchedRepository
import com.example.domain.model.Topic
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val api: SearchApi,
    private val searchMapper: SearchedRepositoryResponseToSearchedRepositoryMapper,
    private val topicMapper: TopicResponseToTopicMapper,
    private val throwableMapper: ThrowableToErrorModelMapper,
) : SearchRepository {
    override suspend fun searchTopics(name: String): Flow<PagingData<Topic>> =
        runCatching {
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                        prefetchDistance = 1,
                    ),
                pagingSourceFactory = {
                    SearchTopicsPagingSource(
                        api = api,
                        name = name,
                        throwableToErrorModelMapper = throwableMapper,
                    )
                },
            ).flow
        }.mapCatching {
            topicMapper.mappingObjects(it)
        }.getOrThrow()

    override suspend fun searchRepositories(name: String): Flow<PagingData<SearchedRepository>> =
        runCatching {
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                        initialLoadSize = 20,
                        prefetchDistance = 10,
                    ),
                pagingSourceFactory = {
                    SearchRepositoriesPagingSource(
                        api = api,
                        name = name,
                        sortOrder = SORT_ORDER,
                        throwableToErrorModelMapper = throwableMapper,
                    )
                },
            ).flow
        }.mapCatching {
            searchMapper.mappingObjects(it)
        }.getOrThrow()
}
