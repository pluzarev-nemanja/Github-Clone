package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.model.LabelResponse
import com.example.data.model.OwnerResponse
import com.example.data.model.RepositoryResponse
import com.example.data.model.SearchedRepositoryWithIssue
import com.example.domain.model.SearchedRepository
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
class SearchedRepositoryResponseToSearchedRepositoryMapperTest {
    private val labelResponseToStringMapper = mockk<LabelResponseToStringMapper>()
    private val searchedRepositoryResponseToSearchedRepositoryMapper =
        SearchedRepositoryResponseToSearchedRepositoryMapper(
            labelResponseToStringMapper,
        )

    @Test
    fun `given SearchedRepositoryResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val searchedRepositoryWithIssue =
                SearchedRepositoryWithIssue(
                    repositories =
                        listOf(
                            RepositoryResponse(
                                id = 1,
                                name = "name",
                                owner = OwnerResponse(authorIcon = "", authorName = "", followers = ""),
                                description = "description",
                                issuesUrl = "",
                                labelsUrl = "",
                            ),
                        ),
                    openIssuesTotalCount = listOf(1),
                    labels =
                        listOf(
                            LabelResponse(
                                color = "",
                                default = false,
                                description = "",
                                id = 0L,
                                name = "",
                                nodeId = "",
                                url = "",
                            ),
                        ),
                )
            coEvery { labelResponseToStringMapper.mappingObjects(any()) } returns listOf("")
            val expected =
                SearchedRepository(
                    name = "name",
                    authorName = "",
                    authorIcon = "",
                    authorFollowers = "",
                    description = "description",
                    issueCount = 1,
                    labelNames = listOf(""),
                )
            val input = flowOf(PagingData.from(listOf(searchedRepositoryWithIssue)))

            val actual =
                searchedRepositoryResponseToSearchedRepositoryMapper.mappingObjects(input)
                    .asSnapshot().first()

            assertEquals(expected, actual)

            coVerify { labelResponseToStringMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
