package com.example.github.pullRequest.mapper

import com.example.domain.model.ErrorResponse
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThrowableToPullRequestDetailsUiStateMapperTest {
    private val throwableToPullRequestDetailsUiStateMapper = ThrowableToPullRequestDetailsUiStateMapper()

    @Test
    fun `given ErrorResponse Network When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Network()

            val actual = throwableToPullRequestDetailsUiStateMapper.mappingObjects(errorResponse)
            val expected = PullRequestDetailsUiState.Error.Connection(message = "Check your internet connection!")

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Host When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Host()

            val actual = throwableToPullRequestDetailsUiStateMapper.mappingObjects(errorResponse)
            val expected = PullRequestDetailsUiState.Error.Server(message = "Something went wrong on server.")

            assertEquals(expected, actual)
        }

    @Test
    fun `given ErrorResponse Unknown When mapper is called Then actual is equal to expected`() =
        runTest {
            val errorResponse = ErrorResponse.Unknown()

            val actual = throwableToPullRequestDetailsUiStateMapper.mappingObjects(errorResponse)
            val expected = PullRequestDetailsUiState.Error.Unknown("Unknown error occurred.")

            assertEquals(expected, actual)
        }
}
