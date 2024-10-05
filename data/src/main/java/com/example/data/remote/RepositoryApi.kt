package com.example.data.remote

import com.example.data.model.CommitResponse
import com.example.data.model.FollowerResponse
import com.example.data.model.IssueResponse
import com.example.data.model.LabelResponse
import com.example.data.model.RepositoryDetailsResponse
import com.example.data.model.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RepositoryApi {
    @GET("/repositories")
    suspend fun getAllRepositories(
        @Query("since") since: Int,
    ): List<RepositoryResponse>

    @GET("/user/repos")
    suspend fun getAuthRepositories(
        @Header("Authorization") token: String,
        @Query("type") ownerShip: String = "owner",
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): List<RepositoryResponse>

    @GET("/repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repository: String,
    ): RepositoryDetailsResponse

    @GET("/repos/{owner}/{repo}/commits")
    suspend fun getRepositoryCommits(
        @Path("owner") owner: String,
        @Path("repo") repository: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): List<CommitResponse>

    @GET
    suspend fun getFollowers(
        @Url url: String,
    ): List<FollowerResponse>

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
