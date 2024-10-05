package com.example.github.pullRequest.viewModel

import androidx.paging.PagingData
import com.example.domain.model.PullRequest
import com.example.domain.model.PullRequestDetails
import com.example.domain.useCases.GetPullRequestDetailsUseCase
import com.example.domain.useCases.GetPullRequestsUseCase
import com.example.github.pullRequest.mapper.PullRequestDetailsToPullRequestDetailsUiModelMapper
import com.example.github.pullRequest.mapper.PullRequestToPullRequestUiModelMapper
import com.example.github.pullRequest.mapper.ThrowableToPullRequestDetailsUiStateMapper
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import com.example.github.pullRequest.model.PullRequestUiModel
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.uiState.PullRequestUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PullRequestViewModelTest {
    private val getPullRequestsUseCase = mockk<GetPullRequestsUseCase>()
    private val getPullRequestDetailsUseCase = mockk<GetPullRequestDetailsUseCase>()
    private val pullRequestUiModelMapper = mockk<PullRequestToPullRequestUiModelMapper>()
    private val pullRequestDetailsToPullRequestDetailsUiModelMapper = mockk<PullRequestDetailsToPullRequestDetailsUiModelMapper>()
    private val throwableToPullRequestDetailsUiStateMapper = mockk<ThrowableToPullRequestDetailsUiStateMapper>(relaxed = true)

    private val pullRequestViewModel =
        PullRequestViewModel(
            getPullRequestsUseCase = getPullRequestsUseCase,
            getPullRequestDetailsUseCase = getPullRequestDetailsUseCase,
            pullRequestUiModelMapper = pullRequestUiModelMapper,
            pullRequestDetailsToPullRequestDetailsUiModelMapper = pullRequestDetailsToPullRequestDetailsUiModelMapper,
            throwableToPullRequestDetailsUiStateMapper = throwableToPullRequestDetailsUiStateMapper,
        )

    @Test
    fun `given owner and repository When getPullRequests is called Then actual is equal to expected`() =
        runTest {
            val owner = "owner"
            val repository = "repository"
            val pullRequest =
                flowOf(
                    PagingData.from(
                        listOf(
                            PullRequest(
                                id = 1,
                                repositoryName = "",
                                ownerName = "",
                                title = "",
                                userImage = "",
                                userName = "",
                                labels = listOf(),
                                requestedReviewers = listOf(),
                            ),
                        ),
                    ),
                )
            val pullRequestUiModel =
                flowOf(
                    PagingData.from(
                        listOf(
                            PullRequestUiModel(
                                id = 1,
                                repositoryName = "",
                                ownerName = "",
                                title = "",
                                userImage = "",
                                userName = "",
                                labels = listOf(),
                                reviewers = listOf(),
                            ),
                        ),
                    ),
                )

            coEvery { getPullRequestsUseCase(any(), any()) } returns pullRequest
            coEvery { pullRequestUiModelMapper.mappingObjects(any()) } returns pullRequestUiModel

            val expected = PullRequestUiState.Success(data = pullRequestUiModel)

            pullRequestViewModel.getPullRequests(owner, repository)

            val actual = pullRequestViewModel.pullRequestUiState.value

            assertEquals(expected, actual)

            coVerify { getPullRequestsUseCase(any(), any()) }
            coVerify { pullRequestUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given exception When getPullRequests is called Then PullRequestUiState is set to Error state`() =
        runTest {
            val owner = "owner"
            val repository = "repository"
            val exception = IllegalStateException("")

            coEvery { getPullRequestsUseCase(any(), any()) } throws exception

            val expected = PullRequestUiState.Error.Unknown(message = "")

            pullRequestViewModel.getPullRequests(owner, repository)

            val actual = pullRequestViewModel.pullRequestUiState.value

            assertEquals(expected, actual)

            coVerify { getPullRequestsUseCase(any(), any()) }
        }

    @Test
    fun `given owner,repository and pullNumber When getPullRequestDetails is called Then actual is equal to expected`() =
        runTest {
            val owner = "owner"
            val repository = "repository"
            val pullNumber = 1

            val pullRequestDetails =
                PullRequestDetails(
                    userName = "",
                    userImage = "",
                    description = "",
                    milestone = "",
                    title = "",
                    labels = listOf(),
                    requestedReviewers = listOf(),
                )
            val pullRequestDetailsUiModel =
                PullRequestDetailsUiModel(
                    description = "",
                    milestone = "",
                    title = "",
                    labels = listOf(),
                    reviewers = listOf(),
                    authorImage = "",
                    authorName = "",
                )

            coEvery { getPullRequestDetailsUseCase(any(), any(), any()) } returns pullRequestDetails
            coEvery { pullRequestDetailsToPullRequestDetailsUiModelMapper.mappingObjects(any()) } returns pullRequestDetailsUiModel

            pullRequestViewModel.getPullRequestDetails(owner, repository, pullNumber)

            val actual = pullRequestViewModel.pullRequestDetailsUiState.value
            val expected =
                PullRequestDetailsUiState.Success(
                    data = pullRequestDetailsUiModel,
                )

            assertEquals(expected, actual)

            coVerify { getPullRequestDetailsUseCase(any(), any(), any()) }
            coVerify { pullRequestDetailsToPullRequestDetailsUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given exception When getPullRequestDetails is called Then PullRequestDetailsUiState is set to Error`() =
        runTest {
            val owner = "owner"
            val repository = "repository"
            val pullNumber = 1

            val spyViewModel = spyk(pullRequestViewModel)

            val exception = IllegalStateException()
            val expected = PullRequestDetailsUiState.Error.Unknown("")

            coEvery { getPullRequestDetailsUseCase(any(), any(), any()) } throws exception
            coEvery { spyViewModel["convertPullError"](exception) } returns expected

            spyViewModel.getPullRequestDetails(owner, repository, pullNumber)

            val actual = spyViewModel.pullRequestDetailsUiState.value

            assertEquals(expected, actual)

            coVerify { getPullRequestDetailsUseCase(any(), any(), any()) }
            coVerify { spyViewModel["convertPullError"](exception) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
