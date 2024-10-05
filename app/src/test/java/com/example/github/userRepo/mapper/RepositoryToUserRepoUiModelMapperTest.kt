package com.example.github.userRepo.mapper

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.domain.model.Repository
import com.example.github.userRepo.model.UserRepoUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryToUserRepoUiModelMapperTest {
    private val repositoryToUserRepoUiModelMapper = RepositoryToUserRepoUiModelMapper()

    @Test
    fun `given Repository When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                flowOf(
                    PagingData.from(
                        listOf(
                            Repository(
                                id = 1,
                                name = "name",
                                authorName = "",
                                authorIcon = "",
                                followers = "",
                                description = "",
                                issuesCount = 1,
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
                    userFollowers = "",
                    repositoryDescription = "",
                    issueCount = "1",
                    labelNames = listOf(),
                )
            val actual = repositoryToUserRepoUiModelMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)
        }

    @Test
    fun `given Repository with description set to null When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                flowOf(
                    PagingData.from(
                        listOf(
                            Repository(
                                id = 1,
                                name = "name",
                                authorName = "",
                                authorIcon = "",
                                followers = "",
                                description = null,
                                issuesCount = 1,
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
                    userFollowers = "",
                    repositoryDescription = "No description found.",
                    issueCount = "1",
                    labelNames = listOf(),
                )
            val actual = repositoryToUserRepoUiModelMapper.mappingObjects(input).asSnapshot().first()

            assertEquals(expected, actual)
        }
}
