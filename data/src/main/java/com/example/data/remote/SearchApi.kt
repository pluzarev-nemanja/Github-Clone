package com.example.data.remote

import com.example.data.model.IssueResponse
import com.example.data.model.LabelResponse
import com.example.data.model.SearchedRepositoryResponse
import com.example.data.model.TopicsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface SearchApi {
    @Headers("Accept: application/vnd.github+json")
    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") name: String,
        @Query("sort") sort: String = "forks",
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): SearchedRepositoryResponse

    @GET("/search/topics")
    suspend fun searchTopics(
        @Query("q") name: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): TopicsResponse

    @GET
    suspend fun getIssues(
        @Url url: String,
        @Query("state") state: String = "open",
    ): List<IssueResponse>

    @GET
    suspend fun getLabels(
        @Url url: String,
    ): List<LabelResponse>
}
