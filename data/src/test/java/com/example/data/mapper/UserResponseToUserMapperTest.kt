package com.example.data.mapper

import com.example.data.model.UserResponse
import com.example.domain.model.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UserResponseToUserMapperTest {
    private val userResponseToUserMapper = UserResponseToUserMapper()

    @Test
    fun `given UserResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val userResponse =
                UserResponse(
                    name = "name",
                    avatarUrl = "",
                    email = "",
                    company = "",
                    biography = "",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            val expected =
                User(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    followers = 1,
                    following = 1,
                    location = "",
                )
            val actual = userResponseToUserMapper.mappingObjects(userResponse)

            assertEquals(expected, actual)
        }
}
