package com.example.data.repository

import com.example.data.mapper.FireBaseUserToAuthUserMapper
import com.example.domain.dataSource.AuthDataSource
import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val fireBaseUserToAuthUserMapper: FireBaseUserToAuthUserMapper,
) : AuthRepository {
    override suspend fun signInWithGithub(token: String): AuthUser? =
        authDataSource.runCatching {
            signInWithGithub(token)
        }.mapCatching { data ->
            fireBaseUserToAuthUserMapper.mappingObjects(data)
        }.getOrThrow()

    override suspend fun getCurrentUser(): AuthUser? =
        authDataSource.runCatching {
            getCurrentUser()
        }.mapCatching { data ->
            fireBaseUserToAuthUserMapper.mappingObjects(data)
        }.getOrThrow()

    override suspend fun signOut() = authDataSource.signOut()
}
