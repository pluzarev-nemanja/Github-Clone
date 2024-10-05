package com.example.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.data.mapper.RepositoryDetailsResponseToRepositoryDetailsMapper
import com.example.data.mapper.RepositoryResponseToRepositoryMapper
import com.example.data.mapper.ThrowableToErrorModelMapper
import com.example.data.model.OwnerResponse
import com.example.data.model.RepositoryDetailsResponse
import com.example.data.remote.RepositoryApi
import com.example.domain.model.Repository
import com.example.domain.model.RepositoryDetails
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReposRepositoryImplTest {
    private val reposMapper = mockk<RepositoryResponseToRepositoryMapper>()
    private val throwableMapper = mockk<ThrowableToErrorModelMapper>(relaxed = true)
    private val api = mockk<RepositoryApi>()
    private val detailsMapper = mockk<RepositoryDetailsResponseToRepositoryDetailsMapper>()
    private var reposRepository =
        ReposRepositoryImpl(
            api = api,
            detailsMapper = detailsMapper,
            reposMapper = reposMapper,
            throwableMapper = throwableMapper,
        )

    @Test
    fun `given RepositoryDetails When getRepositoryDetails is called Then actual is equal to expected`() =
        runTest {
            val expected =
                RepositoryDetails(
                    name = "name",
                    authorIcon = "",
                    authorName = "",
                    description = "asd",
                    forks = 0,
                    watchers = 0,
                    followers = "",
                    topics = listOf("asd", "asd"),
                )
            val repositoryDetailsResponse =
                RepositoryDetailsResponse(
                    name = "name",
                    owner =
                        OwnerResponse(
                            authorIcon = "",
                            authorName = "",
                            followers = "",
                        ),
                    description = "",
                    forks = 1,
                    watchers = 1,
                    topics = listOf(),
                )

            coEvery { api.getRepositoryDetails(any(), any()) } returns repositoryDetailsResponse
            coEvery { detailsMapper.mappingObjects(any()) } returns expected

            val result = reposRepository.getRepositoryDetails("name", "name")

            assertEquals(expected, result)

            coVerify { api.getRepositoryDetails(any(), any()) }
            coVerify { detailsMapper.mappingObjects(any()) }
        }

    @Test
    fun `given token When getAuthRepositories is called Then actual is equal to expected`() =
        runTest {
            val expected =
                Repository(
                    id = 0,
                    name = "asd",
                    authorIcon = "asd",
                    authorName = "ad",
                    followers = "asd",
                    description = null,
                    issuesCount = 0,
                    labelNames = listOf(""),
                )

            coEvery { reposMapper.mappingObjects(any()) } returns
                flowOf(
                    PagingData.from(
                        listOf(
                            expected,
                        ),
                    ),
                )

            val result = reposRepository.getAuthRepositories(token = "asd").asSnapshot().first()

            assertEquals(expected, result)

            coVerify { reposMapper.mappingObjects(any()) }
        }

    @Test
    fun `given Repository When getAllRepositories is called Then actual is equal to expected`() =
        runTest {
            val expected =
                Repository(
                    id = 0,
                    name = "asd",
                    authorIcon = "asd",
                    authorName = "ad",
                    followers = "asd",
                    description = null,
                    issuesCount = 0,
                    labelNames = listOf(""),
                )
            coEvery { reposMapper.mappingObjects(any()) } returns
                flowOf(
                    PagingData.from(
                        listOf(
                            expected,
                        ),
                    ),
                )

            val actual = reposRepository.getAllRepositories().asSnapshot().first()
            assertEquals(expected, actual)

            coVerify { reposMapper.mappingObjects(any()) }
        }

//    @Test
//    fun `given owner and repository name When getRepositoryCommits is called Then actual is equal to expected`() =
//        runTest {
//
//            val owner = "octocat"
//            val repository = "repo"
//            val expected = listOf(0)
//            val commits = listOf(
//                CommitResponse(
//                    commit = Commit(
//                        author = Author(
//                            name = "",
//                            email = "",
//                            date = ""
//                        ),
//                        message = ""
//                    )
//                )
//            )
//            coEvery { api.getRepositoryCommits(any(), any(), any(), any()) } returns commits
//            coEvery { throwableMapper.mappingObjects(any()) } returns mockk()
//
//            val actual = reposRepository.getRepositoryCommits(owner, repository)
//
//            actual.test {
//                assertEquals(expected, awaitItem())
//                awaitComplete()
//            }
//
//
//            coVerify { api.getRepositoryCommits(any(), any(), any(), any()) }
//
//        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
