package com.example.github.userRepo.mapper

import com.example.domain.model.ErrorResponse
import com.example.github.userRepo.uiState.RepositoryDetailsUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThrowableToRepositoryDetailsUiStateErrorMapperTest {
    private val throwableToRepositoryDetailsUiModelMapper = ThrowableToRepositoryDetailsUiStateErrorMapper()

    @Test
    fun `given ErrorResponse Network When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Network()

            val expected = RepositoryDetailsUiState.Error.Connection(message = "Check your internet connection!")

            val actual = throwableToRepositoryDetailsUiModelMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Host When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Host()

            val expected = RepositoryDetailsUiState.Error.Server(message = "Something went wrong on server.")

            val actual = throwableToRepositoryDetailsUiModelMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Unknown When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Unknown()

            val expected = RepositoryDetailsUiState.Error.Unknown("Unknown error occurred.")

            val actual = throwableToRepositoryDetailsUiModelMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }
}
