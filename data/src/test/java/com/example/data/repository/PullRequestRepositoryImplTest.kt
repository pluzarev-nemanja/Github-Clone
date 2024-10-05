package com.example.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.mapper.PullRequestDetailsResponseToPullRequestDetailsMapper
import com.example.data.mapper.PullRequestResponseToPullRequestMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.MilestoneResponse
import com.example.data.model.PullRequestDetailsResponse
import com.example.data.model.User
import com.example.data.remote.PullRequestApi
import com.example.domain.model.PullRequest
import com.example.domain.model.PullRequestDetails
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PullRequestRepositoryImplTest {
    private val api = mockk<PullRequestApi>()
    private val pullRequestMapper = mockk<PullRequestResponseToPullRequestMapper>()
    private val pullRequestDetailsMapper =
        mockk<PullRequestDetailsResponseToPullRequestDetailsMapper>()
    private val throwableMapper = mockk<ThrowableToErrorModelMapper>(relaxed = true)
    private val pullRequestRepositoryImpl =
        PullRequestRepositoryImpl(
            api = api,
            pullRequestMapper = pullRequestMapper,
            pullDetailsMapper = pullRequestDetailsMapper,
            throwableMapper = throwableMapper,
        )

    @Test
    fun `given owner and repository name When getPullRequests is called Then actual is equal to expected`() =
        runTest {
            val owner = "octocat"
            val repositoryName = "repo"
            val expected =
                PullRequest(
                    id = 1,
                    repositoryName = "name",
                    ownerName = "",
                    title = "",
                    userName = "",
                    userImage = "",
                    labels = listOf(),
                    requestedReviewers = listOf(),
                )

            coEvery { pullRequestMapper.mappingObjects(any()) } returns
                flowOf(
                    PagingData.from(
                        listOf(expected),
                    ),
                )
            val actual =
                pullRequestRepositoryImpl.getPullRequests(owner, repositoryName).asSnapshot()
                    .first()

            assertEquals(expected, actual)

            coVerify { pullRequestMapper.mappingObjects(any()) }
        }

    @Test
    fun `given owner,repository name and pullNumber When getPullRequestDetails is called Then actual is equal to expected`() =
        runTest {
            val owner = "octocat"
            val repositoryName = "repo"
            val pullNumber = 1

            val expected =
                PullRequestDetails(
                    userName = "octocat",
                    title = "",
                    userImage = "",
                    labels = listOf(),
                    requestedReviewers = listOf(),
                    description = "",
                    milestone = "",
                )
            val response =
                PullRequestDetailsResponse(
                    title = "",
                    labels = listOf(),
                    requestedReviewers = listOf(),
                    milestone = MilestoneResponse(title = ""),
                    user =
                        User(
                            name = "",
                            avatarUrl = "",
                        ),
                    body = null,
                )

            coEvery { api.getPullRequestDetails(any(), any(), any()) } returns response
            coEvery { pullRequestDetailsMapper.mappingObjects(any()) } returns expected

            val actual =
                pullRequestRepositoryImpl.getPullRequestDetails(
                    owner = owner,
                    repository = repositoryName,
                    pullNumber = pullNumber,
                )

            assertEquals(expected, actual)

            coVerify { pullRequestDetailsMapper.mappingObjects(any()) }
            coVerify { api.getPullRequestDetails(any(), any(), any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
