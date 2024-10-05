package com.example.github.user.uiState

import com.example.github.user.model.UserUiModel
import com.example.github.userRepo.uiState.RepositoryUiState.Error

sealed class UserUiState {
    data object Loading : UserUiState()

    data object Empty : UserUiState()

    data class Success(val data: UserUiModel) : UserUiState()

    sealed class Error : UserUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
