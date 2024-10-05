package com.example.github.topic.uiState

import androidx.paging.PagingData
import com.example.github.topic.model.TopicUiModel
import kotlinx.coroutines.flow.Flow

sealed class TopicsUiState {
    data object Empty : TopicsUiState()

    data object Initial : TopicsUiState()

    data object Loading : TopicsUiState()

    data class Success(val data: Flow<PagingData<TopicUiModel>>) : TopicsUiState()

    sealed class Error : TopicsUiState() {
        open val message: String? = null

        data class Unknown(override val message: String?) : Error()

        data class Connection(override val message: String?) : Error()

        data class Server(override val message: String?) : Error()
    }
}
