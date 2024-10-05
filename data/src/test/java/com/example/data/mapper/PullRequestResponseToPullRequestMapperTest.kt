package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.model.BaseRepositoryResponse
import com.example.data.model.LabelResponse
import com.example.data.model.OwnerResponse
import com.example.data.model.PullRequestResponse
import com.example.data.model.RepositoryResponse
import com.example.data.model.RequestedReviewerResponse
import com.example.data.model.User
import com.example.domain.model.Label
import com.example.domain.model.PullRequest
import com.example.domain.model.RequestedReviewer
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
class PullRequestResponseToPullRequestMapperTest {
    private val labelResponseToLabelMapper = mockk<LabelResponseToLabelMapper>()
    private val reviewerResponseToRequestedReviewerMapper =
        mockk<RequestedReviewerResponseToRequestedReviewerMapper>()

    private val pullRequestResponseToPullRequestMapper =
        PullRequestResponseToPullRequestMapper(
            labelMapper = labelResponseToLabelMapper,
            reviewerMapper = reviewerResponseToRequestedReviewerMapper,
        )

    @Test
    fun `given PullRequestResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val labels =
                listOf(
                    Label(
                        color = "",
                        default = false,
                        description = "description",
                        id = 0L,
                        name = "",
                        nodeId = "",
                        url = "",
                    ),
                )
            val reviewers =
                listOf(
                    RequestedReviewer(avatarUrl = "reviewer", name = "name"),
                )

            coEvery { labelResponseToLabelMapper.mappingObjects(any()) } returns labels
            coEvery { reviewerResponseToRequestedReviewerMapper.mappingObjects(any()) } returns reviewers

            val pullRequestResponse =
                PullRequestResponse(
                    id = 1,
                    baseResponse =
                        BaseRepositoryResponse(
                            repo =
                                RepositoryResponse(
                                    id = 1,
                                    name = "name",
                                    owner =
                                        OwnerResponse(
                                            authorName = "name",
                                            authorIcon = "",
                                            followers = "",
                                        ),
                                    description = "description",
                                    issuesUrl = "",
                                    labelsUrl = "",
                                    openIssueCount = null,
                                ),
                        ),
                    title = "pull-request-title",
                    user = User(avatarUrl = "avatarUrl", name = "name"),
                    labels =
                        listOf(
                            LabelResponse(
                                "",
                                false,
                                "description",
                                0,
                                "",
                                "",
                                "",
                            ),
                        ),
                    requestedReviewers = listOf(RequestedReviewerResponse("reviewer", name = "name")),
                )

            val pullResponse = flowOf(PagingData.from(listOf(pullRequestResponse)))

            val expectedPullRequest =
                PullRequest(
                    id = 1,
                    repositoryName = "name",
                    ownerName = "name",
                    title = "pull-request-title",
                    userImage = "avatarUrl",
                    userName = "name",
                    labels =
                        listOf(
                            Label(
                                "",
                                false,
                                "description",
                                0,
                                "",
                                "",
                                "",
                            ),
                        ),
                    requestedReviewers = listOf(RequestedReviewer("reviewer", name = "name")),
                )

            val actual =
                pullRequestResponseToPullRequestMapper.mappingObjects(pullResponse).asSnapshot()
                    .first()

            assertEquals(expectedPullRequest, actual)

            coVerify { labelResponseToLabelMapper.mappingObjects(any()) }
            coVerify { reviewerResponseToRequestedReviewerMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
