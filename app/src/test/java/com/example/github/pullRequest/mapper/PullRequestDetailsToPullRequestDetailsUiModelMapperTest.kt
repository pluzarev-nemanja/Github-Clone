package com.example.github.pullRequest.mapper

import com.example.domain.model.PullRequestDetails
import com.example.github.pullRequest.model.LabelUiModel
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import com.example.github.pullRequest.model.ReviewerUiModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PullRequestDetailsToPullRequestDetailsUiModelMapperTest {
    private val labelToLabelUiModelMapper = mockk<LabelToLabelUiModelMapper>()
    private val reviewerToReviewerUiModelMapper = mockk<ReviewerToReviewerUiModelMapper>()

    private val pullRequestDetailsToPullRequestDetailsUiModelMapper =
        PullRequestDetailsToPullRequestDetailsUiModelMapper(
            labelToLabelUiModelMapper = labelToLabelUiModelMapper,
            reviewerToReviewerUiModelMapper = reviewerToReviewerUiModelMapper,
        )

    @Test
    fun `given PullRequestDetails When mapper is called Then actual is equal to expected`() =
        runTest {
            val labelUiModel = listOf<LabelUiModel>()

            val requestedReviewerUiModel = listOf<ReviewerUiModel>()
            val pullRequestDetails =
                PullRequestDetails(
                    userImage = "",
                    userName = "",
                    description = "",
                    title = "",
                    labels = listOf(),
                    milestone = "",
                    requestedReviewers = listOf(),
                )

            val expected =
                PullRequestDetailsUiModel(
                    description = "",
                    title = "~  ~",
                    labels = listOf(),
                    milestone = "",
                    authorName = "",
                    authorImage = "",
                    reviewers = listOf(),
                )
            coEvery { labelToLabelUiModelMapper.mappingObjects(any()) } returns labelUiModel
            coEvery { reviewerToReviewerUiModelMapper.mappingObjects(any()) } returns requestedReviewerUiModel

            val actual = pullRequestDetailsToPullRequestDetailsUiModelMapper.mappingObjects(pullRequestDetails)

            assertEquals(expected, actual)

            coVerify { labelToLabelUiModelMapper.mappingObjects(any()) }
            coVerify { reviewerToReviewerUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given PullRequestDetails with null values When mapper is called Then actual is equal to expected`() =
        runTest {
            val labelUiModel = listOf<LabelUiModel>()

            val requestedReviewerUiModel = listOf<ReviewerUiModel>()
            val pullRequestDetails =
                PullRequestDetails(
                    userImage = "",
                    userName = "",
                    description = null,
                    title = "",
                    labels = listOf(),
                    milestone = null,
                    requestedReviewers = listOf(),
                )

            val expected =
                PullRequestDetailsUiModel(
                    description = "No description found.",
                    title = "~  ~",
                    labels = listOf(),
                    milestone = "No milestone found.",
                    authorName = "",
                    authorImage = "",
                    reviewers = listOf(),
                )
            coEvery { labelToLabelUiModelMapper.mappingObjects(any()) } returns labelUiModel
            coEvery { reviewerToReviewerUiModelMapper.mappingObjects(any()) } returns requestedReviewerUiModel

            val actual = pullRequestDetailsToPullRequestDetailsUiModelMapper.mappingObjects(pullRequestDetails)

            assertEquals(expected, actual)

            coVerify { labelToLabelUiModelMapper.mappingObjects(any()) }
            coVerify { reviewerToReviewerUiModelMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
