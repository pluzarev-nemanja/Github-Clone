package com.example.github.pullRequest.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.domain.model.PullRequest
import com.example.github.pullRequest.model.PullRequestUiModel
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
class PullRequestToPullRequestUiModelMapperTest {
    private val labelToLabelUiModelMapper = mockk<LabelToLabelUiModelMapper>()
    private val reviewerToReviewerUiModelMapper = mockk<ReviewerToReviewerUiModelMapper>()

    private val pullRequestToPullRequestUiModelMapper =
        PullRequestToPullRequestUiModelMapper(
            labelToLabelUiModelMapper = labelToLabelUiModelMapper,
            reviewerToReviewerUiModelMapper = reviewerToReviewerUiModelMapper,
        )

    @Test
    fun `given PullRequest When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                flowOf(
                    PagingData.from(
                        listOf(
                            PullRequest(
                                id = 1,
                                requestedReviewers = listOf(),
                                repositoryName = "",
                                ownerName = "",
                                title = "",
                                userImage = "",
                                userName = "",
                                labels = listOf(),
                            ),
                        ),
                    ),
                )

            val expected =
                PullRequestUiModel(
                    id = 1,
                    repositoryName = "",
                    ownerName = "",
                    title = "",
                    userName = "",
                    userImage = "",
                    labels = listOf(),
                    reviewers = listOf(),
                )

            coEvery { labelToLabelUiModelMapper.mappingObjects(any()) } returns listOf()
            coEvery { reviewerToReviewerUiModelMapper.mappingObjects(any()) } returns listOf()

            val actual = pullRequestToPullRequestUiModelMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)

            coVerify { labelToLabelUiModelMapper.mappingObjects(any()) }
            coVerify { reviewerToReviewerUiModelMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
