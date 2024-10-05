package com.example.github.user.mapper

import com.example.domain.model.User
import com.example.github.user.model.UserUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UserToUserUiModelMapperTest {
    private val userToUserUiModelMapper = UserToUserUiModelMapper()

    @Test
    fun `given User When mapper is called Then actual is equal to expected`() =
        runTest {
            val user =
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
            val expected =
                UserUiModel(
                    name = "name",
                    userImage = "",
                    email = "",
                    company = "",
                    biography = "",
                    followers = "1 Followers",
                    following = "1 Following",
                    location = "",
                )

            val actual = userToUserUiModelMapper.mappingObjects(user)

            assertEquals(expected, actual)
        }

    @Test
    fun `given User with null values When mapper is called Then actual is equal to expected`() =
        runTest {
            val user =
                User(
                    name = null,
                    userImage = "",
                    email = null,
                    company = null,
                    biography = null,
                    followers = 1,
                    following = 1,
                    location = null,
                )
            val expected =
                UserUiModel(
                    name = "Unknown",
                    userImage = "",
                    email = "No email found.",
                    company = "No company found.",
                    biography = "No biography found.",
                    followers = "1 Followers",
                    following = "1 Following",
                    location = "No location found.",
                )

            val actual = userToUserUiModelMapper.mappingObjects(user)

            assertEquals(expected, actual)
        }
}
