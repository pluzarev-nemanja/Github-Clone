package com.example.github.userRepo.uiState

import kotlinx.coroutines.flow.Flow

sealed class CommitUiState {
    data object Loading : CommitUiState()

    data object Empty : CommitUiState()

    data class Success(val data: Flow<List<Int>>) : CommitUiState()

    sealed class Error : CommitUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
