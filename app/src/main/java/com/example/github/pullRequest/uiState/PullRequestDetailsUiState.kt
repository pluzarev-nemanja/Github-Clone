package com.example.github.pullRequest.uiState

import com.example.github.pullRequest.model.PullRequestDetailsUiModel

sealed class PullRequestDetailsUiState {
    data object Loading : PullRequestDetailsUiState()

    data object Empty : PullRequestDetailsUiState()

    data class Success(val data: PullRequestDetailsUiModel) : PullRequestDetailsUiState()

    sealed class Error : PullRequestDetailsUiState() {
        open val message: String? = null

        data class Connection(override val message: String?) : Error()

        data class Unknown(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
