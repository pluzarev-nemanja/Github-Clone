package com.example.github.userRepo.mapper

import com.example.domain.model.RepositoryDetails
import com.example.github.userRepo.model.RepositoryDetailsUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryDetailsToRepositoryDetailsUiModelMapperTest {
    private val repositoryDetailsToRepositoryDetailsUiModelMapper =
        RepositoryDetailsToRepositoryDetailsUiModelMapper()

    @Test
    fun `given RepositoryDetails When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                RepositoryDetails(
                    name = "name",
                    authorName = "",
                    authorIcon = "",
                    followers = "1",
                    description = "",
                    forks = 1,
                    watchers = 1,
                    topics = listOf(),
                )

            val expected =
                RepositoryDetailsUiModel(
                    repositoryName = "name",
                    userName = "",
                    userIcon = "",
                    userFollowers = "1 Followers",
                    repositoryDescription = "",
                    repositoryForks = "1 Forks",
                    watchers = "1 Watchers",
                    topics = listOf(),
                )
            val actual = repositoryDetailsToRepositoryDetailsUiModelMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }

    @Test
    fun `given RepositoryDetails with description set to null When mapper is called Then actual is equal to expected`() =
        runTest {
            val input =
                RepositoryDetails(
                    name = "name",
                    authorName = "",
                    authorIcon = "",
                    followers = "1",
                    description = null,
                    forks = 1,
                    watchers = 1,
                    topics = listOf(),
                )

            val expected =
                RepositoryDetailsUiModel(
                    repositoryName = "name",
                    userName = "",
                    userIcon = "",
                    userFollowers = "1 Followers",
                    repositoryDescription = "No description found.",
                    repositoryForks = "1 Forks",
                    watchers = "1 Watchers",
                    topics = listOf(),
                )
            val actual = repositoryDetailsToRepositoryDetailsUiModelMapper.mappingObjects(input)

            assertEquals(expected, actual)
        }
}
