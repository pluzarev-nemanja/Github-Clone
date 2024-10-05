package com.example.data.remote

import com.example.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {
    @GET("/users/{username}")
    suspend fun getUser(
        @Path("username") username: String,
    ): UserResponse

    @GET("/user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") token: String,
    ): UserResponse
}
