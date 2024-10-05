package com.example.github.auth.mapper

import com.example.domain.model.AuthUser
import com.example.github.auth.model.AuthUserUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthUserToAuthUserUiModelMapperTest {
    private val authUserUiModelMapper = AuthUserToAuthUserUiModelMapper()

    @Test
    fun `given AuthUser When mapper is called Then actual is equal to expected`() =
        runTest {
            val authUser =
                AuthUser(
                    uid = "",
                    displayName = "",
                    email = "",
                )
            val expected =
                AuthUserUiModel(
                    uid = "",
                    displayName = "",
                    email = "",
                )

            val actual = authUserUiModelMapper.mappingObjects(authUser)

            assertEquals(expected, actual)
        }

    @Test
    fun `given AuthUser with null properties When mapper is called Then actual is equal to expected`() =
        runTest {
            val authUser =
                AuthUser(
                    uid = "",
                    displayName = null,
                    email = null,
                )
            val expected =
                AuthUserUiModel(
                    uid = "",
                    displayName = "No display name found.",
                    email = "No email found.",
                )

            val actual = authUserUiModelMapper.mappingObjects(authUser)

            assertEquals(expected, actual)
        }
}
