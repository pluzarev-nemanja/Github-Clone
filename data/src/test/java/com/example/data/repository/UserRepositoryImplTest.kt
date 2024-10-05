package com.example.data.repository

import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.mapper.UserResponseToUserMapper
import com.example.data.model.UserResponse
import com.example.data.remote.UserApi
import com.example.domain.model.User
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRepositoryImplTest {
    private val userApi = mockk<UserApi>()
    private val userMapper = mockk<UserResponseToUserMapper>()
    private val throwableMapper = mockk<ThrowableToErrorModelMapper>(relaxed = true)
    private val userRepositoryImpl =
        UserRepositoryImpl(
            api = userApi,
            userMapper = userMapper,
            throwableMapper = throwableMapper,
        )

    @Test
    fun `given userName When getUser function is called Then actual is equal to expected`() =
        runTest {
            val userName = "octocat"
            val expected =
                User(
                    name = "octocat",
                    userImage = "",
                    email = "email",
                    company = "company",
                    biography = "bio",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            val response =
                UserResponse(
                    name = "octocat",
                    avatarUrl = "",
                    email = "email",
                    company = "company",
                    biography = "bio",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            coEvery { userApi.getUser(any()) } returns response
            coEvery { userMapper.mappingObjects(any()) } returns expected

            val actual = userRepositoryImpl.getUser(userName)
            assertEquals(expected, actual)

            coVerify { userApi.getUser(any()) }
            coVerify { userMapper.mappingObjects(any()) }
        }

    @Test
    fun `given token When getAuthenticatedUser is called Then actual is equal to expected`() =
        runTest {
            val token = "token"
            val expected =
                User(
                    name = "octocat",
                    userImage = "",
                    email = "email",
                    company = "company",
                    biography = "bio",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            val response =
                UserResponse(
                    name = "octocat",
                    avatarUrl = "",
                    email = "email",
                    company = "company",
                    biography = "bio",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            coEvery { userApi.getAuthenticatedUser(any()) } returns response
            coEvery { userMapper.mappingObjects(any()) } returns expected

            val actual = userRepositoryImpl.getAuthenticatedUser(token)
            assertEquals(expected, actual)

            coVerify { userApi.getAuthenticatedUser(any()) }
            coVerify { userMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
