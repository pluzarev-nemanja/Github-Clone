package com.example.github.pullRequest.uiState

import androidx.paging.PagingData
import com.example.github.pullRequest.model.PullRequestUiModel
import kotlinx.coroutines.flow.Flow

sealed class PullRequestUiState {
    data object Loading : PullRequestUiState()

    data object Empty : PullRequestUiState()

    data class Success(val data: Flow<PagingData<PullRequestUiModel>>) : PullRequestUiState()

    sealed class Error : PullRequestUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()
    }
}
