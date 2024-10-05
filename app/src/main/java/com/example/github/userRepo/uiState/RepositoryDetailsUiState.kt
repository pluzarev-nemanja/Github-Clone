package com.example.github.userRepo.uiState

import com.example.github.userRepo.model.RepositoryDetailsUiModel

sealed class RepositoryDetailsUiState {
    data object Loading : RepositoryDetailsUiState()

    data object Empty : RepositoryDetailsUiState()

    data class Success(val data: RepositoryDetailsUiModel) : RepositoryDetailsUiState()

    sealed class Error : RepositoryDetailsUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
