package com.example.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.mapper.SearchedRepositoryResponseToSearchedRepositoryMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.mapper.TopicResponseToTopicMapper
import com.example.data.remote.SearchApi
import com.example.domain.model.SearchedRepository
import com.example.domain.model.Topic
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
class SearchRepositoryImplTest {
    private val api = mockk<SearchApi>()
    private val searchMapper = mockk<SearchedRepositoryResponseToSearchedRepositoryMapper>()
    private val topicsMapper = mockk<TopicResponseToTopicMapper>()
    private val throwableMapper = mockk<ThrowableToErrorModelMapper>(relaxed = true)

    private val searchRepositoryImpl =
        SearchRepositoryImpl(
            api = api,
            searchMapper = searchMapper,
            topicMapper = topicsMapper,
            throwableMapper = throwableMapper,
        )

    @Test
    fun `given name When searchTopics function is called Then actual is equal to expected`() =
        runTest {
            val name = "topic"
            val expected =
                Topic(
                    totalCount = 1,
                    name = "topic",
                    shortDescription = "",
                    createdBy = "",
                    createdAt = "",
                )
            coEvery { topicsMapper.mappingObjects(any()) } returns
                flowOf(
                    PagingData.from(
                        listOf(
                            expected,
                        ),
                    ),
                )
            val actual = searchRepositoryImpl.searchTopics(name).asSnapshot().first()

            assertEquals(expected, actual)
            coVerify { topicsMapper.mappingObjects(any()) }
        }

    @Test
    fun `given name When searchRepositories function is called Then actual is equal to expected`() =
        runTest {
            val name = "repo"
            val expected =
                SearchedRepository(
                    name = "repository",
                    authorName = "",
                    authorIcon = "",
                    authorFollowers = "",
                    description = "",
                    issueCount = 1,
                    labelNames = listOf(),
                )
            coEvery { searchMapper.mappingObjects(any()) } returns
                flowOf(
                    PagingData.from(
                        listOf(
                            expected,
                        ),
                    ),
                )
            val actual = searchRepositoryImpl.searchRepositories(name).asSnapshot().first()

            assertEquals(expected, actual)

            coVerify { searchMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
