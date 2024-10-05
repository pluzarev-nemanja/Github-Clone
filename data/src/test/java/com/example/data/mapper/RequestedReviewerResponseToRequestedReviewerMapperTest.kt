package com.example.data.mapper

import com.example.data.model.RequestedReviewerResponse
import com.example.domain.model.RequestedReviewer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RequestedReviewerResponseToRequestedReviewerMapperTest {
    private val requestedReviewerResponseToRequestedReviewerMapper =
        RequestedReviewerResponseToRequestedReviewerMapper()

    @Test
    fun `given RequestedReviewerResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                listOf(
                    RequestedReviewerResponse(
                        avatarUrl = "",
                        name = "name",
                    ),
                )
            val expected =
                listOf(
                    RequestedReviewer(
                        avatarUrl = "",
                        name = "name",
                    ),
                )
            val actual = requestedReviewerResponseToRequestedReviewerMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }

    @Test
    fun `given empty list When mapper is called Then actual is equal to expected`() =
        runTest {
            val actual = requestedReviewerResponseToRequestedReviewerMapper.mappingObjects(emptyList())
            assertEquals(emptyList<RequestedReviewer>(), actual)
        }
}
