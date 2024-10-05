package com.example.domain.repository

import com.example.domain.model.AuthUser

interface AuthRepository {
    suspend fun signInWithGithub(token: String): AuthUser?

    suspend fun getCurrentUser(): AuthUser?

    suspend fun signOut()
}
