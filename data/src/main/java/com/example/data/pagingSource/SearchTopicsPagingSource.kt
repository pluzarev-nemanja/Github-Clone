package com.example.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.TopicsResponse
import com.example.data.remote.SearchApi

class SearchTopicsPagingSource(
    private val api: SearchApi,
    private val name: String,
    private val throwableToErrorModelMapper: ThrowableToErrorModelMapper,
) : PagingSource<Int, TopicsResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopicsResponse> =
        runCatching {
            val page = params.key ?: 1
            val response =
                api.searchTopics(
                    name = name,
                    pageSize = params.loadSize,
                    page = page,
                )
            LoadResult.Page(
                data = listOf(response),
                prevKey = if (page == 1) null else page.dec(),
                nextKey = if (response.topics.isEmpty()) null else page.inc(),
            )
        }.getOrElse { throwable ->
            LoadResult.Error(throwableToErrorModelMapper.mappingObjects(throwable))
        }

    override fun getRefreshKey(state: PagingState<Int, TopicsResponse>): Int? =
        state.run {
            anchorPosition?.let {
                closestPageToPosition(it)
            }?.run {
                prevKey?.inc() ?: nextKey?.dec()
            }
        }
}
