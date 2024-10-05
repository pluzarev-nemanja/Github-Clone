package com.example.data.pagingSource

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.LabelResponse
import com.example.data.model.RepositoryWithIssue
import com.example.data.remote.RepositoryApi
import timber.log.Timber

class GetAuthRepositoriesPagingSource(
    private val repositoryApi: RepositoryApi,
    private val token: String,
    private val throwableToErrorModelMapper: ThrowableToErrorModelMapper,
) : PagingSource<Int, RepositoryWithIssue>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryWithIssue> =
        runCatching {
            val issuesCount = mutableListOf<Int>()
            val labels = mutableListOf<LabelResponse>()

            val response =
                repositoryApi.getAuthRepositories(
                    token = token,
                    pageSize = 1,
                    page = params.key ?: 1,
                )

            response.forEach {
                issuesCount.add(it.openIssueCount ?: 0)
                labels += (repositoryApi.getLabels(it.labelsUrl.replace("{/name}", "")))
            }

            LoadResult.Page(
                data =
                    listOf(
                        RepositoryWithIssue(
                            repositories = response,
                            openIssuesTotalCount = issuesCount,
                            labels = labels,
                        ),
                    ),
                prevKey = if ((params.key ?: 1) == 1) null else (params.key ?: 1).dec(),
                nextKey = if (response.isEmpty()) null else (params.key ?: 1).inc(),
            )
        }.getOrElse { throwable ->
            Timber.e(throwable)
            LoadResult.Error(throwableToErrorModelMapper.mappingObjects(throwable))
        }

    override fun getRefreshKey(state: PagingState<Int, RepositoryWithIssue>): Int? =
        state.run {
            anchorPosition?.let {
                closestPageToPosition(it)
            }?.run {
                prevKey?.inc() ?: nextKey?.dec()
            }
        }
}
