package com.example.data.remote

import com.example.data.model.PullRequestDetailsResponse
import com.example.data.model.PullRequestResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PullRequestApi {
    @GET("/repos/{owner}/{repo}/pulls")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repository: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): List<PullRequestResponse>

    @GET("/repos/{owner}/{repo}/pulls/{pull_number}")
    suspend fun getPullRequestDetails(
        @Path("owner") owner: String,
        @Path("repo") repository: String,
        @Path("pull_number") pullNumber: Int,
    ): PullRequestDetailsResponse
}
