package com.example.github.pullRequest.viewModel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.domain.model.PullRequest
import com.example.domain.model.PullRequestDetails
import com.example.domain.useCases.GetPullRequestDetailsUseCase
import com.example.domain.useCases.GetPullRequestsUseCase
import com.example.github.pullRequest.mapper.PullRequestDetailsToPullRequestDetailsUiModelMapper
import com.example.github.pullRequest.mapper.PullRequestToPullRequestUiModelMapper
import com.example.github.pullRequest.mapper.ThrowableToPullRequestDetailsUiStateMapper
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import com.example.github.pullRequest.model.PullRequestUiModel
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState
import com.example.github.pullRequest.uiState.PullRequestUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PullRequestViewModel(
    private val getPullRequestsUseCase: GetPullRequestsUseCase,
    private val getPullRequestDetailsUseCase: GetPullRequestDetailsUseCase,
    private val pullRequestUiModelMapper: PullRequestToPullRequestUiModelMapper,
    private val pullRequestDetailsToPullRequestDetailsUiModelMapper: PullRequestDetailsToPullRequestDetailsUiModelMapper,
    private val throwableToPullRequestDetailsUiStateMapper: ThrowableToPullRequestDetailsUiStateMapper,
) : ViewModel() {
    private val mutablePullRequestUiState: MutableStateFlow<PullRequestUiState> =
        MutableStateFlow(PullRequestUiState.Loading)

    val pullRequestUiState: StateFlow<PullRequestUiState> = mutablePullRequestUiState.asStateFlow()

    private val mutablePullRequestDetailsUiState: MutableStateFlow<PullRequestDetailsUiState> =
        MutableStateFlow(PullRequestDetailsUiState.Loading)

    val pullRequestDetailsUiState: StateFlow<PullRequestDetailsUiState> =
        mutablePullRequestDetailsUiState.asStateFlow()

    init {
        getPullRequests(
            owner = "mojombo",
            repository = "god",
        )
    }

    fun getPullRequests(
        owner: String,
        repository: String,
    ) {
        viewModelScope.launch {
            mutablePullRequestUiState.value = PullRequestUiState.Loading

            runCatching {
                getPullRequestsUseCase(owner = owner, repository = repository)
            }.mapCatching { pullRequestList: Flow<PagingData<PullRequest>> ->
                pullRequestUiModelMapper.mappingObjects(pullRequestList)
            }.mapCatching { pullRequestUiModel: Flow<PagingData<PullRequestUiModel>> ->
                mutablePullRequestUiState.value =
                    PullRequestUiState.Success(
                        data = pullRequestUiModel,
                    )
            }.onFailure {
                mutablePullRequestUiState.value =
                    PullRequestUiState.Error.Unknown(message = it.message)
                Timber.e(it, "Error occurred")
            }
        }
    }

    fun getPullRequestDetails(
        owner: String,
        repository: String,
        pullNumber: Int,
    ) {
        viewModelScope.launch {
            mutablePullRequestDetailsUiState.value = PullRequestDetailsUiState.Loading

            runCatching {
                getPullRequestDetailsUseCase(
                    owner = owner,
                    repository = repository,
                    pullNumber = pullNumber,
                )
            }.mapCatching { pullRequestDetails: PullRequestDetails ->
                pullRequestDetailsToPullRequestDetailsUiModelMapper.mappingObjects(
                    pullRequestDetails,
                )
            }.mapCatching { pullRequest: PullRequestDetailsUiModel ->
                mutablePullRequestDetailsUiState.value =
                    PullRequestDetailsUiState.Success(
                        data = pullRequest,
                    )
            }.onFailure { exception ->
                mutablePullRequestDetailsUiState.value = exception.convertPullError()
                Timber.e(exception, "Error occurred")
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun Throwable.convertPullError(): PullRequestDetailsUiState.Error =
        throwableToPullRequestDetailsUiStateMapper.runCatching {
            throwableToPullRequestDetailsUiStateMapper.mappingObjects(this@convertPullError)
        }.onFailure { throwable ->
            Timber.e("Error : $throwable")
        }.getOrThrow()
}
