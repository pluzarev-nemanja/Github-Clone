package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun getUser(username: String): User

    suspend fun getAuthenticatedUser(token: String): User
}
