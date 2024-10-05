package com.example.github.topic.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.domain.model.Topic
import com.example.github.topic.model.TopicUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopicToTopicUiModelMapperTest {
    private val topicToTopicUiModelMapper = TopicToTopicUiModelMapper()

    @Test
    fun `given Topic When mapper is called Then actual is equal to expected`() =
        runTest {
            val topic =
                flowOf(
                    PagingData.from(
                        listOf(
                            Topic(
                                totalCount = 1,
                                name = "",
                                shortDescription = "",
                                createdAt = "",
                                createdBy = "",
                            ),
                        ),
                    ),
                )

            val expected =
                TopicUiModel(
                    totalCont = 1,
                    name = "",
                    shortDescription = "",
                    createdBy = "",
                    createdAt = "",
                )

            val actual = topicToTopicUiModelMapper.mappingObjects(topic).asSnapshot().first()

            assertEquals(expected, actual)
        }

    @Test
    fun `given Topic with null values When mapper is called Then actual is equal to expected`() =
        runTest {
            val topic =
                flowOf(
                    PagingData.from(
                        listOf(
                            Topic(
                                totalCount = 1,
                                name = null,
                                shortDescription = null,
                                createdAt = null,
                                createdBy = null,
                            ),
                        ),
                    ),
                )

            val expected =
                TopicUiModel(
                    totalCont = 1,
                    name = "Unknown",
                    shortDescription = "No description found.",
                    createdBy = "Unknown",
                    createdAt = "No date found.",
                )

            val actual = topicToTopicUiModelMapper.mappingObjects(topic).asSnapshot().first()

            assertEquals(expected, actual)
        }
}
