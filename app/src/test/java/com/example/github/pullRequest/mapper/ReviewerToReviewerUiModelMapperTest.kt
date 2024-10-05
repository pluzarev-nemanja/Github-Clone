package com.example.github.pullRequest.mapper

import com.example.domain.model.RequestedReviewer
import com.example.github.pullRequest.model.ReviewerUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReviewerToReviewerUiModelMapperTest {
    private val reviewerToReviewerUiModelMapper = ReviewerToReviewerUiModelMapper()

    @Test
    fun `given Reviewer When mapper is called Then actual is equal to expected`() =
        runTest {
            val reviewers =
                listOf(
                    RequestedReviewer(
                        avatarUrl = "",
                        name = "name",
                    ),
                )

            val expected =
                listOf(
                    ReviewerUiModel(
                        reviewerImage = "",
                        reviewerName = "name",
                    ),
                )

            val actual = reviewerToReviewerUiModelMapper.mappingObjects(reviewers)

            assertEquals(expected, actual)
        }
}
