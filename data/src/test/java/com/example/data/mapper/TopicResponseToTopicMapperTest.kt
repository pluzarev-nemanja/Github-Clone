package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.model.TopicResponse
import com.example.data.model.TopicsResponse
import com.example.domain.model.Topic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopicResponseToTopicMapperTest {
    private val topicResponseToTopicMapper = TopicResponseToTopicMapper()

    @Test
    fun `given TopicResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val topicResponse =
                TopicsResponse(
                    incompleteResults = false,
                    topics =
                        listOf(
                            TopicResponse(
                                name = "name",
                                shortDescription = "",
                                createdAt = "",
                                createdBy = "",
                            ),
                        ),
                    totalCount = 1,
                )
            val input = flowOf(PagingData.from(listOf(topicResponse)))
            val expected =
                Topic(
                    totalCount = 1,
                    name = "name",
                    shortDescription = "",
                    createdAt = "",
                    createdBy = "",
                )
            val actual = topicResponseToTopicMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)
        }
}
