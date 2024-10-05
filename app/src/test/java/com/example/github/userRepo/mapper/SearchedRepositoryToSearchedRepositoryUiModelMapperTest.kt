package com.example.github.userRepo.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.domain.model.SearchedRepository
import com.example.github.userRepo.model.UserRepoUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchedRepositoryToSearchedRepositoryUiModelMapperTest {
    private val searchedRepositoryToSearchedRepositoryUiModelMapper = SearchedRepositoryToSearchedRepositoryUiModelMapper()

    @Test
    fun `given SearchedRepository When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                flowOf(
                    PagingData.from(
                        listOf(
                            SearchedRepository(
                                name = "name",
                                authorName = "",
                                authorIcon = "",
                                authorFollowers = "1",
                                description = "desc",
                                issueCount = 1,
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            val expected =
                UserRepoUiModel(
                    id = 1,
                    repositoryName = "name",
                    userName = "",
                    userIcon = "",
                    userFollowers = "1",
                    repositoryDescription = "desc",
                    issueCount = "1",
                    labelNames = listOf(),
                )
            val actual = searchedRepositoryToSearchedRepositoryUiModelMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)
        }

    @Test
    fun `given SearchedRepository with null set for description When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                flowOf(
                    PagingData.from(
                        listOf(
                            SearchedRepository(
                                name = "name",
                                authorName = "",
                                authorIcon = "",
                                authorFollowers = "1",
                                description = null,
                                issueCount = 1,
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            val expected =
                UserRepoUiModel(
                    id = 1,
                    repositoryName = "name",
                    userName = "",
                    userIcon = "",
                    userFollowers = "1",
                    repositoryDescription = "No description found.",
                    issueCount = "1",
                    labelNames = listOf(),
                )
            val actual = searchedRepositoryToSearchedRepositoryUiModelMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)
        }
}
