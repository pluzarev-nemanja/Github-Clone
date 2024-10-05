package com.example.data.pagingSource

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.PullRequestResponse
import com.example.data.remote.PullRequestApi

class GetPullRequestPagingSource(
    private val api: PullRequestApi,
    private val owner: String,
    private val repository: String,
    private val throwableToErrorModelMapper: ThrowableToErrorModelMapper,
) : PagingSource<Int, PullRequestResponse>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PullRequestResponse> =
        runCatching {
            val response =
                api.getPullRequests(
                    owner = owner,
                    repository = repository,
                    pageSize = params.loadSize,
                    page = params.key ?: 1,
                )
            LoadResult.Page(
                data = response,
                prevKey = if ((params.key ?: 1) == 1) null else (params.key ?: 1).dec(),
                nextKey = if (response.isEmpty()) null else (params.key ?: 1).inc(),
            )
        }.getOrElse { throwable ->
            LoadResult.Error(throwableToErrorModelMapper.mappingObjects(throwable))
        }

    override fun getRefreshKey(state: PagingState<Int, PullRequestResponse>): Int? =
        state.run {
            anchorPosition?.let {
                closestPageToPosition(it)
            }?.run {
                prevKey?.inc() ?: nextKey?.dec()
            }
        }
}
