package com.example.github.userRepo.viewModel

import androidx.paging.PagingData
import com.example.domain.model.Repository
import com.example.domain.model.RepositoryDetails
import com.example.domain.model.SearchedRepository
import com.example.domain.repository.AnalyticsRepository
import com.example.domain.repository.ReposRepository
import com.example.domain.repository.SearchRepository
import com.example.domain.useCases.GetAllRepositoriesUseCase
import com.example.domain.useCases.GetAuthRepositoriesUseCase
import com.example.domain.useCases.GetRepositoryCommitsUseCase
import com.example.domain.useCases.GetRepositoryDetailsUseCase
import com.example.domain.useCases.LogEventUseCase
import com.example.domain.useCases.SearchRepositoriesUseCase
import com.example.github.userRepo.mapper.RepositoryDetailsToRepositoryDetailsUiModelMapper
import com.example.github.userRepo.mapper.RepositoryToUserRepoUiModelMapper
import com.example.github.userRepo.mapper.SearchedRepositoryToSearchedRepositoryUiModelMapper
import com.example.github.userRepo.mapper.ThrowableToCommitUiStateErrorMapper
import com.example.github.userRepo.mapper.ThrowableToRepositoryDetailsUiStateErrorMapper
import com.example.github.userRepo.model.RepositoryDetailsUiModel
import com.example.github.userRepo.model.UserRepoUiModel
import com.example.github.userRepo.uiState.CommitUiState
import com.example.github.userRepo.uiState.RepositoryDetailsUiState
import com.example.github.userRepo.uiState.RepositoryUiState
import com.example.github.userRepo.uiState.SearchedRepositoryUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserRepoViewModelTest {
    private val reposRepository = mockk<ReposRepository>()
    private val searchRepository = mockk<SearchRepository>()
    private val analyticsRepository = mockk<AnalyticsRepository>()
    private val getAllRepositoriesUseCase = spyk(GetAllRepositoriesUseCase(reposRepository))
    private val getAuthRepositoriesUseCase = spyk(GetAuthRepositoriesUseCase(reposRepository))
    private val getRepositoryDetailsUseCase = spyk(GetRepositoryDetailsUseCase(reposRepository))
    private val searchRepositoriesUseCase = spyk(SearchRepositoriesUseCase(searchRepository))
    private val getRepositoryCommitsUseCase = spyk(GetRepositoryCommitsUseCase(reposRepository))
    private val logEventUseCase = spyk(LogEventUseCase(analyticsRepository))
    private val repositoryToUserRepoUiModelMapper = mockk<RepositoryToUserRepoUiModelMapper>()
    private val searchedRepositoryToSearchedRepositoryUiModelMapper =
        mockk<SearchedRepositoryToSearchedRepositoryUiModelMapper>()
    private val throwableToRepositoryDetailsUiStateErrorMapper =
        mockk<ThrowableToRepositoryDetailsUiStateErrorMapper>(relaxed = true)
    private val throwableToCommitUiStateErrorMapper =
        mockk<ThrowableToCommitUiStateErrorMapper>(relaxed = true)
    private val repositoryDetailsToRepositoryDetailsUiModelMapper =
        mockk<RepositoryDetailsToRepositoryDetailsUiModelMapper>()

    private val userRepoViewModel =
        UserRepoViewModel(
            getAllRepositoriesUseCase = getAllRepositoriesUseCase,
            getAuthRepositoriesUseCase = getAuthRepositoriesUseCase,
            getRepositoryDetailsUseCase = getRepositoryDetailsUseCase,
            searchRepositoriesUseCase = searchRepositoriesUseCase,
            getRepositoryCommitsUseCase = getRepositoryCommitsUseCase,
            logEventUseCase = logEventUseCase,
            repositoryToUserRepoUiModelMapper = repositoryToUserRepoUiModelMapper,
            repositoryDetailsToRepositoryDetailsUiModelMapper = repositoryDetailsToRepositoryDetailsUiModelMapper,
            searchedRepositoryToSearchedRepositoryUiModelMapper = searchedRepositoryToSearchedRepositoryUiModelMapper,
            throwableToCommitUiStateErrorMapper = throwableToCommitUiStateErrorMapper,
            throwableToRepositoryDetailsUiStateErrorMapper = throwableToRepositoryDetailsUiStateErrorMapper,
        )

    @Test
    fun `when getAllRepositories is called Then RepositoryUiState is set to Success`() =
        runTest {
            val repository =
                flowOf(
                    PagingData.from(
                        listOf(
                            Repository(
                                id = 1,
                                name = "name",
                                authorIcon = "",
                                authorName = "",
                                followers = "",
                                description = "",
                                issuesCount = 1,
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            val userRepoUiModel =
                flowOf(
                    PagingData.from(
                        listOf(
                            UserRepoUiModel(
                                id = 1,
                                repositoryDescription = "",
                                repositoryName = "name",
                                userFollowers = "",
                                userIcon = "",
                                userName = "",
                                issueCount = "1",
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            coEvery { getAllRepositoriesUseCase() } returns repository
            coEvery { repositoryToUserRepoUiModelMapper.mappingObjects(any()) } returns userRepoUiModel

            userRepoViewModel.getAllRepositories()

            val expectedUiState = RepositoryUiState.Success(data = userRepoUiModel)
            val actualUiState = userRepoViewModel.repositoryUiState.value

            assertEquals(
                expectedUiState,
                actualUiState,
            )

            coVerify { getAllRepositoriesUseCase() }
            coVerify { repositoryToUserRepoUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `when getAllRepositories is called with exception Then RepositoryUiState is set to Error`() =
        runTest {
            val exception = IllegalStateException()

            coEvery { getAllRepositoriesUseCase() } throws exception

            userRepoViewModel.getAllRepositories()

            val expectedUiState = RepositoryUiState.Error.Unknown("Unknown error occurred")
            val actualUiState = userRepoViewModel.repositoryUiState.value

            assertEquals(expectedUiState, actualUiState)

            coVerify { getAllRepositoriesUseCase() }
        }

    @Test
    fun `given token When getAuthRepositories is called Then RepositoryUiState is set to Success`() =
        runTest {
            val token = "token"
            val repository =
                flowOf(
                    PagingData.from(
                        listOf(
                            Repository(
                                id = 1,
                                name = "name",
                                authorIcon = "",
                                authorName = "",
                                followers = "",
                                description = "",
                                issuesCount = 1,
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            val userRepoUiModel =
                flowOf(
                    PagingData.from(
                        listOf(
                            UserRepoUiModel(
                                id = 1,
                                repositoryDescription = "",
                                repositoryName = "name",
                                userFollowers = "",
                                userIcon = "",
                                userName = "",
                                issueCount = "1",
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )
            coEvery { getAuthRepositoriesUseCase(any()) } returns repository
            coEvery { repositoryToUserRepoUiModelMapper.mappingObjects(any()) } returns userRepoUiModel

            userRepoViewModel.getAuthRepositories(token)

            val expectedUiState = RepositoryUiState.Success(data = userRepoUiModel)
            val actualUiState = userRepoViewModel.repositoryUiState.value

            assertEquals(
                expectedUiState,
                actualUiState,
            )

            coVerify { getAuthRepositoriesUseCase(any()) }
            coVerify { repositoryToUserRepoUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `when getAuthRepositories is called with exception Then RepositoryUiState is set to Error`() =
        runTest {
            val exception = IllegalStateException()
            val token = "token"
            coEvery { getAuthRepositoriesUseCase(any()) } throws exception

            userRepoViewModel.getAuthRepositories(token)

            val expectedUiState = RepositoryUiState.Error.Unknown("Unknown error occurred")
            val actualUiState = userRepoViewModel.repositoryUiState.value

            assertEquals(expectedUiState, actualUiState)

            coVerify { getAuthRepositoriesUseCase(any()) }
        }

    @Test
    fun `given owner and repositoryName When getRepositoryDetails is called Then RepositoryDetailsUiState is set to Success`() =
        runTest {
            val owner = "owner"
            val repository = "repository"
            val repositoryDetails =
                RepositoryDetails(
                    name = "name",
                    authorName = "",
                    authorIcon = "",
                    followers = "",
                    description = "",
                    forks = 1,
                    watchers = 1,
                    topics = listOf(),
                )

            val repositoryDetailsUiModel =
                RepositoryDetailsUiModel(
                    repositoryName = "name",
                    userName = "",
                    userFollowers = "",
                    userIcon = "",
                    repositoryDescription = "",
                    repositoryForks = "",
                    watchers = "",
                    topics = listOf(),
                )

            coEvery { getRepositoryDetailsUseCase(any(), any()) } returns repositoryDetails
            coEvery { repositoryDetailsToRepositoryDetailsUiModelMapper.mappingObjects(any()) } returns repositoryDetailsUiModel

            val expectedUiState = RepositoryDetailsUiState.Success(data = repositoryDetailsUiModel)

            userRepoViewModel.getRepositoryDetails(owner, repository)

            val actualUiState = userRepoViewModel.repositoryDetailsUiState.value

            assertEquals(expectedUiState, actualUiState)

            coVerify { getRepositoryDetailsUseCase(any(), any()) }
            coVerify { repositoryDetailsToRepositoryDetailsUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `when getRepositoryDetails is called with exception Then RepositoryDetailsUiState is set to Error`() =
        runTest {
            val spyViewModel = spyk(userRepoViewModel)

            val exception = IllegalStateException()
            val expected = RepositoryDetailsUiState.Error.Connection("")

            coEvery { getRepositoryDetailsUseCase(any(), any()) } throws exception
            coEvery {
                spyViewModel["convertRepositoryDetailsError"](exception)
            } returns expected

            spyViewModel.getRepositoryDetails("owner", "repo")

            val actual = spyViewModel.repositoryDetailsUiState.value

            assertEquals(expected, actual)

            coVerify { getRepositoryDetailsUseCase(any(), any()) }
            coVerify {
                spyViewModel["convertRepositoryDetailsError"](exception)
            }
        }

    @Test
    fun `when searchRepositories is called Then SearchedRepositoryUiState is set to Success`() =
        runTest {
            val name = "name"

            val userRepoUiModel =
                flowOf(
                    PagingData.from(
                        listOf(
                            UserRepoUiModel(
                                id = 1,
                                repositoryDescription = "",
                                repositoryName = "name",
                                userFollowers = "",
                                userIcon = "",
                                userName = "",
                                issueCount = "1",
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            val searchedRepository =
                flowOf(
                    PagingData.from(
                        listOf(
                            SearchedRepository(
                                description = "",
                                name = "name",
                                authorFollowers = "",
                                authorIcon = "",
                                authorName = "",
                                issueCount = 1,
                                labelNames = listOf(),
                            ),
                        ),
                    ),
                )

            coEvery { searchRepositoriesUseCase(any()) } returns searchedRepository
            coEvery { searchedRepositoryToSearchedRepositoryUiModelMapper.mappingObjects(any()) } returns userRepoUiModel

            userRepoViewModel.searchRepositories(name)

            val expected = SearchedRepositoryUiState.Success(data = userRepoUiModel)
            val actual = userRepoViewModel.searchedRepositoryUiState.value

            assertEquals(expected, actual)

            coVerify { searchRepositoriesUseCase(any()) }
            coVerify { searchedRepositoryToSearchedRepositoryUiModelMapper.mappingObjects(any()) }
        }

    @Test
    fun `when searchRepositories is called with exception Then SearchedRepositoryUiState is set to Error`() =
        runTest {
            val name = "name"

            val expected = SearchedRepositoryUiState.Error.Unknown("Unknown error occurred")

            coEvery { searchRepositoriesUseCase(any()) } throws IllegalArgumentException()

            userRepoViewModel.searchRepositories(name)
            val actual = userRepoViewModel.searchedRepositoryUiState.value

            assertEquals(expected, actual)

            coVerify { searchRepositoriesUseCase(any()) }
        }

    @Test
    fun `given owner and repository name When getRepositoryCommits is called Then CommitUiState is set to Success`() =
        runTest {
            val owner = "owner"
            val repository = "repo"

            val list = flowOf(listOf(1))

            coEvery { getRepositoryCommitsUseCase(any(), any()) } returns list

            userRepoViewModel.getRepositoryCommits(owner, repository)

            val actual = userRepoViewModel.commitUiState.value
            val expected = CommitUiState.Success(data = list)

            assertEquals(expected, actual)

            coVerify { getRepositoryCommitsUseCase(any(), any()) }
        }

    @Test
    fun `when getRepositoryCommits is called with Exception Then CommitUiState is set to Error`() =
        runTest {
            val spyViewModel = spyk(userRepoViewModel)

            val owner = "owner"
            val repository = "repository"
            val exception = IllegalStateException()
            val expected = CommitUiState.Error.Unknown("")

            coEvery { getRepositoryCommitsUseCase(any(), any()) } throws exception
            coEvery { spyViewModel["convertRepositoryCommitsError"](exception) } returns expected

            spyViewModel.getRepositoryCommits(owner, repository)

            val actual = spyViewModel.commitUiState.value

            assertEquals(expected, actual)

            coVerify { getRepositoryCommitsUseCase(any(), any()) }
            coVerify { spyViewModel["convertRepositoryCommitsError"](exception) }
        }

    @Test
    fun `given empty query When updateSearchQuery is called Then actual is equal to expected`() =
        runTest {
            val query = ""

            userRepoViewModel.updateSearchQuery(query)
            val actual = userRepoViewModel.searchedRepositoryUiState.value
            val expected = SearchedRepositoryUiState.Initial

            assertEquals(expected, actual)
        }

    @Test
    fun `given itemCount and isDataInErrorState When isRepositoryListEmpty is called Then actual is equal to expected`() =
        runTest {
            val itemCount = 0
            val isDataInErrorState = false

            userRepoViewModel.isRepositoryListEmpty(itemCount, isDataInErrorState)

            val actual = userRepoViewModel.searchedRepositoryUiState.value
            val expected = SearchedRepositoryUiState.Empty

            assertEquals(expected, actual)
        }

    @Test
    fun `given itemCount is set to 1 and isDataInErrorState When isRepositoryListEmpty is called Then actual is equal to expected`() =
        runTest {
            val itemCount = 1
            val isDataInErrorState = false

            userRepoViewModel.isRepositoryListEmpty(itemCount, isDataInErrorState)

            val actual = userRepoViewModel.searchedRepositoryUiState.value
            val expected = SearchedRepositoryUiState.Initial

            assertEquals(expected, actual)
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
