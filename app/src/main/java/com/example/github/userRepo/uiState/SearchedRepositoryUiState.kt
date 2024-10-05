package com.example.github.userRepo.uiState

import androidx.paging.PagingData
import com.example.github.userRepo.model.UserRepoUiModel
import com.example.github.userRepo.uiState.RepositoryDetailsUiState.Error
import kotlinx.coroutines.flow.Flow

sealed class SearchedRepositoryUiState {
    data object Loading : SearchedRepositoryUiState()

    data object Initial : SearchedRepositoryUiState()

    data object Empty : SearchedRepositoryUiState()

    data class Success(val data: Flow<PagingData<UserRepoUiModel>>) :
        SearchedRepositoryUiState()

    sealed class Error : SearchedRepositoryUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
