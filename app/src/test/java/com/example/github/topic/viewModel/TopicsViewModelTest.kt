package com.example.github.topic.viewModel

import androidx.paging.PagingData
import com.example.domain.model.Topic
import com.example.domain.useCases.SearchTopicsUseCase
import com.example.github.topic.mapper.TopicToTopicUiModelMapper
import com.example.github.topic.model.TopicUiModel
import com.example.github.topic.uiState.TopicsUiState
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
class TopicsViewModelTest {
    private val searchTopicsUseCase = mockk<SearchTopicsUseCase>()
    private val topicToTopicUiModelMapper = mockk<TopicToTopicUiModelMapper>()

    private val topicsViewModel =
        TopicsViewModel(
            searchTopicsUseCase = searchTopicsUseCase,
            topicToTopicUiModelMapper = topicToTopicUiModelMapper,
        )

    @Test
    fun `given itemCount and isDataInErrorState  When isTopicsListEmpty is called Then actual is equal to expected`() =
        runTest {
            val itemCount = 0
            val isDataInErrorState = false

            val expected = TopicsUiState.Empty

            topicsViewModel.isTopicsListEmpty(itemCount, isDataInErrorState)

            val actual = topicsViewModel.topicsUiState.value

            assertEquals(expected, actual)
        }

    @Test
    fun `given empty query When updateSearchQuery is called Then actual is equal to expected`() =
        runTest {
            val query = ""
            val expected = TopicsUiState.Initial

            topicsViewModel.updateSearchQuery(query)
            val actual = topicsViewModel.topicsUiState.value

            assertEquals(expected, actual)
        }

    @Test
    fun `given query When updateSearchQuery is called Then actual is equal to expected`() =
        runTest {
            val expected = "query"

            topicsViewModel.updateSearchQuery(expected)
            val actual = topicsViewModel.searchQuery.value

            assertEquals(expected, actual)
        }

    @Test
    fun `given name When searchTopics is called Then actual is equal to expected`() =
        runTest {
            val name = "name"
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
            val topicsUiModel =
                flowOf(
                    PagingData.from(
                        listOf(
                            TopicUiModel(
                                totalCont = 1,
                                name = "Unknown",
                                shortDescription = "No description found.",
                                createdBy = "Unknown",
                                createdAt = "No date found.",
                            ),
                        ),
                    ),
                )
            coEvery { searchTopicsUseCase(any()) } returns topic
            coEvery { topicToTopicUiModelMapper.mappingObjects(any()) } returns topicsUiModel

            val expected = TopicsUiState.Success(data = topicsUiModel)

            topicsViewModel.searchTopics(name)

            val actual = topicsViewModel.topicsUiState.value

            assertEquals(expected, actual)

            coVerify { searchTopicsUseCase(any()) }
            coVerify { topicToTopicUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `given name with exception When searchTopics is called Then actual is equal to expected`() =
        runTest {
            val name = "name"
            val exception = IllegalStateException()

            coEvery { searchTopicsUseCase(any()) } throws exception

            topicsViewModel.searchTopics(name)

            val actual = topicsViewModel.topicsUiState.value
            val expected = TopicsUiState.Error.Unknown("Unknown error occurred")

            assertEquals(expected, actual)

            coVerify { searchTopicsUseCase(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
