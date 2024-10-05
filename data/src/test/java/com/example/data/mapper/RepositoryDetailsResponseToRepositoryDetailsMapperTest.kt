package com.example.data.mapper

import com.example.data.model.FollowerResponse
import com.example.data.model.OwnerResponse
import com.example.data.model.RepositoryDetailsResponse
import com.example.data.remote.RepositoryApi
import com.example.domain.model.RepositoryDetails
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryDetailsResponseToRepositoryDetailsMapperTest {
    private val api = mockk<RepositoryApi>()
    private val followersResponseToIntMapper = mockk<FollowersResponseToIntMapper>()

    private val repositoryDetailsResponseToRepositoryDetailsMapper =
        RepositoryDetailsResponseToRepositoryDetailsMapper(
            api = api,
            followersResponseToIntMapper = followersResponseToIntMapper,
        )

    @Test
    fun `given RepositoryDetailsResponse When mapper is called Then actual is equal to expected`() =
        runTest {
            val repositoryDetailsResponse =
                RepositoryDetailsResponse(
                    name = "name",
                    owner = OwnerResponse(authorName = "", authorIcon = "", followers = ""),
                    description = "",
                    forks = 0,
                    watchers = 0,
                    topics = listOf(),
                )
            val expected =
                RepositoryDetails(
                    name = "name",
                    authorName = "",
                    authorIcon = "",
                    forks = 0,
                    description = "",
                    followers = "1",
                    watchers = 0,
                    topics = listOf(),
                )
            coEvery { api.getFollowers(any()) } returns listOf(FollowerResponse(name = "John"))
            coEvery { followersResponseToIntMapper.mappingObjects(any()) } returns 1

            val actual =
                repositoryDetailsResponseToRepositoryDetailsMapper.mappingObjects(
                    repositoryDetailsResponse,
                )

            assertEquals(expected, actual)

            coVerify { api.getFollowers(any()) }
            coEvery { followersResponseToIntMapper.mappingObjects(any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
