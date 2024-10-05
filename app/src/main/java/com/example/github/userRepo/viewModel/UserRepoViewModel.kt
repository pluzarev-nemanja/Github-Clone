package com.example.github.userRepo.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.domain.model.Repository
import com.example.domain.model.RepositoryDetails
import com.example.domain.model.SearchedRepository
import com.example.domain.model.TrackingEvent
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class UserRepoViewModel(
    private val getAllRepositoriesUseCase: GetAllRepositoriesUseCase,
    private val getAuthRepositoriesUseCase: GetAuthRepositoriesUseCase,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val getRepositoryCommitsUseCase: GetRepositoryCommitsUseCase,
    private val logEventUseCase: LogEventUseCase,
    private val repositoryToUserRepoUiModelMapper: RepositoryToUserRepoUiModelMapper,
    private val repositoryDetailsToRepositoryDetailsUiModelMapper: RepositoryDetailsToRepositoryDetailsUiModelMapper,
    private val searchedRepositoryToSearchedRepositoryUiModelMapper: SearchedRepositoryToSearchedRepositoryUiModelMapper,
    private val throwableToRepositoryDetailsUiStateErrorMapper: ThrowableToRepositoryDetailsUiStateErrorMapper,
    private val throwableToCommitUiStateErrorMapper: ThrowableToCommitUiStateErrorMapper,
) : ViewModel() {
    private val mutableRepositoryUiState: MutableStateFlow<RepositoryUiState> =
        MutableStateFlow(RepositoryUiState.Loading)

    val repositoryUiState: StateFlow<RepositoryUiState> =
        mutableRepositoryUiState.asStateFlow()

    private val mutableRepositoryDetailsUiState: MutableStateFlow<RepositoryDetailsUiState> =
        MutableStateFlow(RepositoryDetailsUiState.Loading)

    val repositoryDetailsUiState: StateFlow<RepositoryDetailsUiState> =
        mutableRepositoryDetailsUiState.asStateFlow()

    private val mutableSearchedRepositoryUiState: MutableStateFlow<SearchedRepositoryUiState> =
        MutableStateFlow(SearchedRepositoryUiState.Initial)

    val searchedRepositoryUiState: StateFlow<SearchedRepositoryUiState> =
        mutableSearchedRepositoryUiState.asStateFlow()

    private var mutableSearchQuery: MutableStateFlow<String> =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> = mutableSearchQuery.asStateFlow()

    private val mutableCommitUiState: MutableStateFlow<CommitUiState> =
        MutableStateFlow(CommitUiState.Loading)
    val commitUiState: StateFlow<CommitUiState> = mutableCommitUiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        if (query.isEmpty()) {
            mutableSearchedRepositoryUiState.value =
                SearchedRepositoryUiState.Initial
        }
        mutableSearchQuery.value = query
    }

    fun isRepositoryListEmpty(
        itemCount: Int,
        isDataInErrorState: Boolean,
    ) {
        if (itemCount == 0 && isDataInErrorState.not()) {
            mutableSearchedRepositoryUiState.value =
                SearchedRepositoryUiState.Empty
        }
    }

    fun getAllRepositories() {
        viewModelScope.launch {
            mutableRepositoryUiState.value = RepositoryUiState.Loading

            runCatching {
                getAllRepositoriesUseCase()
            }.mapCatching { repository: Flow<PagingData<Repository>> ->

                repositoryToUserRepoUiModelMapper.mappingObjects(repository)
            }.mapCatching { userRepoUIModel: Flow<PagingData<UserRepoUiModel>> ->
                mutableRepositoryUiState.value = RepositoryUiState.Success(data = userRepoUIModel)
            }.onFailure { exception ->
                mutableRepositoryUiState.value =
                    RepositoryUiState.Error.Unknown("Unknown error occurred")
                Timber.e(exception, "Error occurred : $exception")
            }
        }
    }

    fun getAuthRepositories(token: String) {
        viewModelScope.launch {
            mutableRepositoryUiState.value = RepositoryUiState.Loading

            runCatching {
                getAuthRepositoriesUseCase(token = token)
            }.mapCatching { repository: Flow<PagingData<Repository>> ->

                repositoryToUserRepoUiModelMapper.mappingObjects(repository)
            }.mapCatching { userRepoUIModel: Flow<PagingData<UserRepoUiModel>> ->
                mutableRepositoryUiState.value = RepositoryUiState.Success(data = userRepoUIModel)
            }.onFailure { exception ->
                mutableRepositoryUiState.value =
                    RepositoryUiState.Error.Unknown("Unknown error occurred")
                Timber.e(exception, "Error occurred : $exception")
            }
        }
    }

    fun getRepositoryDetails(
        owner: String,
        repository: String,
    ) {
        viewModelScope.launch {
            mutableRepositoryDetailsUiState.value = RepositoryDetailsUiState.Loading

            runCatching {
                getRepositoryDetailsUseCase(owner = owner, repository = repository)
            }.mapCatching { repositoryDetails: RepositoryDetails ->
                repositoryDetailsToRepositoryDetailsUiModelMapper.mappingObjects(repositoryDetails)
            }.mapCatching { repositoryDetailsUIModel: RepositoryDetailsUiModel ->
                mutableRepositoryDetailsUiState.value =
                    RepositoryDetailsUiState.Success(data = repositoryDetailsUIModel)
            }.onFailure { exception ->
                mutableRepositoryDetailsUiState.value = exception.convertRepositoryDetailsError()
                Timber.e(exception, "Error occurred : $exception")
            }
        }
    }

    fun searchRepositories(name: String) {
        viewModelScope.launch {
            mutableSearchedRepositoryUiState.value = SearchedRepositoryUiState.Loading

            runCatching {
                searchRepositoriesUseCase(name = name)
            }.mapCatching { searchedRepositories: Flow<PagingData<SearchedRepository>> ->

                searchedRepositoryToSearchedRepositoryUiModelMapper.mappingObjects(
                    searchedRepositories,
                )
            }.mapCatching { searchedRepositoryUIModel: Flow<PagingData<UserRepoUiModel>> ->
                mutableSearchedRepositoryUiState.value =
                    SearchedRepositoryUiState.Success(
                        data = searchedRepositoryUIModel,
                    )
            }.onFailure { exception ->
                mutableSearchedRepositoryUiState.value =
                    SearchedRepositoryUiState.Error.Unknown("Unknown error occurred")
                Timber.e(exception, "Error occurred ${mutableSearchedRepositoryUiState.value}")
            }
        }
    }

    fun getRepositoryCommits(
        owner: String,
        repository: String,
    ) {
        viewModelScope.launch {
            mutableCommitUiState.value = CommitUiState.Loading

            runCatching {
                getRepositoryCommitsUseCase(
                    owner = owner,
                    repository = repository,
                )
            }.mapCatching { commit: Flow<List<Int>> ->
                mutableCommitUiState.value =
                    CommitUiState.Success(
                        data = commit,
                    )
            }.onFailure { exception ->
                mutableCommitUiState.value = exception.convertRepositoryCommitsError()
                Timber.e(exception, "Error occurred ${mutableSearchedRepositoryUiState.value}")
            }
        }
    }

    fun logEvent(trackingEvent: TrackingEvent) {
        viewModelScope.launch {
            runCatching {
                logEventUseCase(trackingEvent = trackingEvent)
            }.onFailure {
                Timber.e("Error in viewModel")
            }.getOrThrow()
        }
    }

    @VisibleForTesting
    suspend fun Throwable.convertRepositoryDetailsError(): RepositoryDetailsUiState.Error =
        throwableToRepositoryDetailsUiStateErrorMapper.runCatching {
            this.mappingObjects(this@convertRepositoryDetailsError)
        }.onFailure { throwable ->
            Timber.e("Error : $throwable")
        }.getOrThrow()

    @VisibleForTesting
    suspend fun Throwable.convertRepositoryCommitsError(): CommitUiState.Error =
        throwableToCommitUiStateErrorMapper.runCatching {
            this.mappingObjects(this@convertRepositoryCommitsError)
        }.onFailure { throwable: Throwable ->
            Timber.e("Error: $throwable")
        }.getOrThrow()
}
