package com.example.data.mapper

import com.example.data.model.LabelResponse
import com.example.data.model.MilestoneResponse
import com.example.data.model.PullRequestDetailsResponse
import com.example.data.model.RequestedReviewerResponse
import com.example.data.model.User
import com.example.domain.model.Label
import com.example.domain.model.PullRequestDetails
import com.example.domain.model.RequestedReviewer
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class PullRequestDetailsResponseToPullRequestDetailsMapperTest {
    private val labelMapper = mockk<LabelResponseToLabelMapper>()
    private val reviewerMapper = mockk<RequestedReviewerResponseToRequestedReviewerMapper>()
    private val milestoneMapper = mockk<MilestoneResponseToMilestoneMapper>()

    private val pullRequestDetailsResponseToPullRequestDetailsMapper =
        PullRequestDetailsResponseToPullRequestDetailsMapper(
            labelMapper = labelMapper,
            reviewerMapper = reviewerMapper,
            milestoneMapper = milestoneMapper,
        )

    @Test
    fun `given PullRequestDetailsResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val labels =
                listOf(
                    Label(
                        color = "",
                        default = true,
                        description = "",
                        id = 0L,
                        name = "asd",
                        nodeId = "asd",
                        url = "asd",
                    ),
                )
            val requestedReviewersResponse =
                listOf(RequestedReviewerResponse(avatarUrl = "", name = ""))
            val requestedReviewers = listOf(RequestedReviewer(avatarUrl = "", name = ""))
            val milestoneResponse = MilestoneResponse(title = "milestone")
            val milestone = "milestone"

            val input =
                PullRequestDetailsResponse(
                    user =
                        User(
                            name = "name",
                            avatarUrl = "asd",
                        ),
                    title = "title",
                    body = "",
                    labels =
                        listOf(
                            LabelResponse(
                                color = "",
                                default = true,
                                description = "",
                                id = 0L,
                                name = "asd",
                                nodeId = "asd",
                                url = "asd",
                            ),
                        ),
                    milestone = milestoneResponse,
                    requestedReviewers = requestedReviewersResponse,
                )
            val expected =
                PullRequestDetails(
                    userName = "name",
                    userImage = "asd",
                    description = "",
                    title = "title",
                    labels =
                        listOf(
                            Label(
                                color = "",
                                default = true,
                                description = "",
                                id = 0L,
                                name = "asd",
                                nodeId = "asd",
                                url = "asd",
                            ),
                        ),
                    milestone = "milestone",
                    requestedReviewers = requestedReviewers,
                )

            coEvery { labelMapper.mappingObjects(any()) } returns labels
            coEvery { reviewerMapper.mappingObjects(any()) } returns requestedReviewers
            coEvery { milestoneMapper.mappingObjects(any()) } returns milestone

            val actual = pullRequestDetailsResponseToPullRequestDetailsMapper.mappingObjects(input)

            assertEquals(expected, actual)

            coVerify { labelMapper.mappingObjects(any()) }
            coVerify { reviewerMapper.mappingObjects(any()) }
            coVerify { milestoneMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
