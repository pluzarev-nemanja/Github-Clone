package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.mapper.PullRequestDetailsResponseToPullRequestDetailsMapper
import com.example.data.mapper.PullRequestResponseToPullRequestMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.pagingSource.GetPullRequestPagingSource
import com.example.data.remote.PullRequestApi
import com.example.data.util.Constants.PAGE_SIZE
import com.example.domain.model.PullRequest
import com.example.domain.model.PullRequestDetails
import com.example.domain.repository.PullRequestRepository
import kotlinx.coroutines.flow.Flow

class PullRequestRepositoryImpl(
    private val api: PullRequestApi,
    private val pullRequestMapper: PullRequestResponseToPullRequestMapper,
    private val pullDetailsMapper: PullRequestDetailsResponseToPullRequestDetailsMapper,
    private val throwableMapper: ThrowableToErrorModelMapper,
) : PullRequestRepository {
    override suspend fun getPullRequests(
        owner: String,
        repository: String,
    ): Flow<PagingData<PullRequest>> =
        runCatching {
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                        prefetchDistance = 1,
                    ),
                pagingSourceFactory = {
                    GetPullRequestPagingSource(
                        api = api,
                        owner = owner,
                        repository = repository,
                        throwableToErrorModelMapper = throwableMapper,
                    )
                },
            ).flow
        }.mapCatching {
            pullRequestMapper.mappingObjects(it)
        }.getOrThrow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getPullRequestDetails(
        owner: String,
        repository: String,
        pullNumber: Int,
    ): PullRequestDetails =
        api.runCatching {
            getPullRequestDetails(
                owner = owner,
                repository = repository,
                pullNumber = pullNumber,
            )
        }.mapCatching {
            pullDetailsMapper.mappingObjects(it)
        }.getOrElse { exception: Throwable ->
            throw throwableMapper.mappingObjects(exception)
        }
}
