package com.example.github.userRepo.uiState

import androidx.paging.PagingData
import com.example.github.userRepo.model.UserRepoUiModel
import kotlinx.coroutines.flow.Flow

sealed class RepositoryUiState {
    data object Loading : RepositoryUiState()

    data object Empty : RepositoryUiState()

    data class Success(val data: Flow<PagingData<UserRepoUiModel>>) : RepositoryUiState()

    sealed class Error : RepositoryUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
