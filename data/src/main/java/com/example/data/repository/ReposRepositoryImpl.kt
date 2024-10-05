package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.mapper.RepositoryDetailsResponseToRepositoryDetailsMapper
import com.example.data.mapper.RepositoryResponseToRepositoryMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.pagingSource.GetAllRepositoriesPagingSource
import com.example.data.pagingSource.GetAuthRepositoriesPagingSource
import com.example.data.remote.RepositoryApi
import com.example.data.util.Constants.DATE_FORMAT
import com.example.data.util.Constants.DAY_FORMAT
import com.example.data.util.Constants.PAGE_SIZE
import com.example.domain.model.Repository
import com.example.domain.model.RepositoryDetails
import com.example.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class ReposRepositoryImpl(
    private val api: RepositoryApi,
    private val detailsMapper: RepositoryDetailsResponseToRepositoryDetailsMapper,
    private val reposMapper: RepositoryResponseToRepositoryMapper,
    private val throwableMapper: ThrowableToErrorModelMapper,
) : ReposRepository {
    override suspend fun getAllRepositories(): Flow<PagingData<Repository>> =
        runCatching {
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                        prefetchDistance = 1,
                    ),
                pagingSourceFactory = { GetAllRepositoriesPagingSource(api = api, throwableMapper) },
            ).flow
        }.mapCatching {
            reposMapper.mappingObjects(it)
        }.getOrElse {
            throw throwableMapper.mappingObjects(it)
        }

    override suspend fun getRepositoryCommits(
        owner: String,
        repository: String,
    ): Flow<List<Int>> =
        runCatching {
            val response =
                api.getRepositoryCommits(
                    owner = owner,
                    repository = repository,
                    pageSize = 100,
                    page = 1,
                )
            val dailyCommitCounts: MutableList<Int> = mutableListOf()
            val calendar =
                Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -30)
                }
            val endDate = calendar.time

            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val dayFormat = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())

            val commitPerDay =
                response.groupBy {
                    dateFormat.parse(it.commit.author.date)?.let { date ->
                        dayFormat.format(date)
                    } ?: ""
                }.mapValues {
                    it.value.size
                }

            val firstCommitDate =
                response.mapNotNull {
                    dateFormat.parse(it.commit.author.date)
                }.minOrNull() ?: endDate

            Calendar.getInstance().apply {
                time = firstCommitDate
                while (time.before(endDate)) {
                    val day = dayFormat.format(time)
                    dailyCommitCounts.add(commitPerDay[day] ?: 0)
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            flowOf(dailyCommitCounts.take(30))
        }.getOrElse { exception: Throwable ->
            throw throwableMapper.mappingObjects(exception)
        }

    override suspend fun getAuthRepositories(token: String): Flow<PagingData<Repository>> =
        runCatching {
            Pager(
                config =
                    PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    GetAuthRepositoriesPagingSource(
                        repositoryApi = api,
                        throwableToErrorModelMapper = throwableMapper,
                        token = token,
                    )
                },
            ).flow
        }.mapCatching { repository ->
            reposMapper.mappingObjects(repository)
        }.getOrElse { exception ->
            throw throwableMapper.mappingObjects(exception)
        }

    override suspend fun getRepositoryDetails(
        owner: String,
        repository: String,
    ): RepositoryDetails =
        api.runCatching {
            getRepositoryDetails(
                owner = owner,
                repository = repository,
            )
        }.mapCatching {
            detailsMapper.mappingObjects(it)
        }.getOrElse { exception: Throwable ->
            throw throwableMapper.mappingObjects(exception)
        }
}
