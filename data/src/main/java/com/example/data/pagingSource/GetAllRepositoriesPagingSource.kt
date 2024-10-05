package com.example.data.pagingSource

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.LabelResponse
import com.example.data.model.RepositoryWithIssue
import com.example.data.remote.RepositoryApi

/**
 * @property since is used for pagination.Initially set to 1 and it is updated on last elements id because pages 1 and 2,for example,has same responses.So
 * to prevent repositories to occur on our screen twice,since is set to last elements Id.
 * @property takeItems is used in take function to load exact number of repositories to prevent api to load 30 items at once as it is set to default.
 * @author Nemanja
 */

class GetAllRepositoriesPagingSource(
    private val api: RepositoryApi,
    private val throwableToErrorModelMapper: ThrowableToErrorModelMapper,
) : PagingSource<Int, RepositoryWithIssue>() {
    private var since = 1

    @SuppressLint("NewApi")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryWithIssue> =
        runCatching {
            val issuesCount = mutableListOf<Int>()
            val labels = mutableListOf<LabelResponse>()
            val takeItems = 5
            val response =
                api.getAllRepositories(
                    since = since,
                ).take(takeItems)

            since = response.last().id

            response.forEach {
                issuesCount.add(api.getIssues(it.issuesUrl.replace("{/number}", "")).size)
                labels += (api.getLabels(it.labelsUrl.replace("{/name}", "")))
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
