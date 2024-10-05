package com.example.data.repository

import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.mapper.UserResponseToUserMapper
import com.example.data.remote.UserApi
import com.example.domain.model.User
import com.example.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi,
    private val userMapper: UserResponseToUserMapper,
    private val throwableMapper: ThrowableToErrorModelMapper,
) : UserRepository {
    override suspend fun getUser(username: String): User =
        api.runCatching {
            getUser(username = username)
        }.mapCatching {
            userMapper.mappingObjects(it)
        }.getOrElse { exception: Throwable ->
            throw throwableMapper.mappingObjects(exception)
        }

    override suspend fun getAuthenticatedUser(token: String): User =
        api.runCatching {
            getAuthenticatedUser(token)
        }.mapCatching {
            userMapper.mappingObjects(it)
        }.getOrElse { exception: Throwable ->
            throw throwableMapper.mappingObjects(exception)
        }
}
