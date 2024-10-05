package com.example.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.LabelResponse
import com.example.data.model.SearchedRepositoryWithIssue
import com.example.data.remote.SearchApi

class SearchRepositoriesPagingSource(
    private val api: SearchApi,
    private val name: String,
    private val sortOrder: String,
    private val throwableToErrorModelMapper: ThrowableToErrorModelMapper,
) : PagingSource<Int, SearchedRepositoryWithIssue>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchedRepositoryWithIssue> =
        runCatching {
            val page = params.key ?: 1
            val response =
                api.searchRepositories(
                    name = name,
                    sort = sortOrder,
                    pageSize = params.loadSize,
                    page = page,
                )

            val issuesCount = mutableListOf<Int>()
            val labels = mutableListOf<LabelResponse>()

            response.repositories.take(1).forEach {
                issuesCount.add(api.getIssues(it.issuesUrl.replace("{/number}", "")).size)
                labels += (api.getLabels(it.labelsUrl.replace("{/name}", "")))
            }

            LoadResult.Page(
                data =
                    listOf(
                        SearchedRepositoryWithIssue(
                            repositories = response.repositories.take(1),
                            openIssuesTotalCount = issuesCount,
                            labels = labels,
                        ),
                    ),
                prevKey = if (page == 1) null else page.dec(),
                nextKey = if (response.repositories.isEmpty()) null else page.inc(),
            )
        }.getOrElse { throwable ->
            LoadResult.Error(throwableToErrorModelMapper.mappingObjects(throwable))
        }

    override fun getRefreshKey(state: PagingState<Int, SearchedRepositoryWithIssue>): Int? =
        state.run {
            anchorPosition?.let {
                closestPageToPosition(it)
            }?.run {
                prevKey?.inc() ?: nextKey?.dec()
            }
        }
}
