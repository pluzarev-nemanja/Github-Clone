package com.example.github.user.mapper

import com.example.domain.model.ErrorResponse
import com.example.github.user.uiState.UserUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ThrowableToUserUiStateErrorMapperTest {
    private val throwableToUserUiModelMapper = ThrowableToUserUiStateErrorMapper()

    @Test
    fun `given ErrorResponse Network When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Network()

            val expected = UserUiState.Error.Connection(message = "Check your internet connection!")

            val actual = throwableToUserUiModelMapper.mappingObjects(errorResponse)

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Host When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Host()

            val expected = UserUiState.Error.Server(message = "Something went wrong on server.")

            val actual = throwableToUserUiModelMapper.mappingObjects(errorResponse)

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Unknown When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Unknown()

            val expected = UserUiState.Error.Unknown("Unknown error occurred.")

            val actual = throwableToUserUiModelMapper.mappingObjects(errorResponse)

            assertEquals(expected, actual)
        }
}
