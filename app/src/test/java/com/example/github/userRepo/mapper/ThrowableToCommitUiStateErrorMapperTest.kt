package com.example.github.userRepo.mapper

import com.example.domain.model.ErrorResponse
import com.example.github.userRepo.uiState.CommitUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThrowableToCommitUiStateErrorMapperTest {
    private val throwableToCommitUiStateErrorMapper = ThrowableToCommitUiStateErrorMapper()

    @Test
    fun `given ErrorResponse Network When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Network()
            val expected =
                CommitUiState.Error.Connection(message = "Check your internet connection!")
            val actual = throwableToCommitUiStateErrorMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Host When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Host()
            val expected = CommitUiState.Error.Server(message = "Something went wrong on server.")
            val actual = throwableToCommitUiStateErrorMapper.mappingObjects(input)
            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Unknown When mapper is called Then actual is equal to expected`() =
        runTest {
            val input = ErrorResponse.Unknown()
            val expected = CommitUiState.Error.Unknown("Unknown error occurred.")
            val actual = throwableToCommitUiStateErrorMapper.mappingObjects(input)
            assertEquals(expected, actual)
        }
}
