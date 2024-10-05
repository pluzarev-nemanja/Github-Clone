package com.example.github.topic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.domain.model.Topic
import com.example.domain.useCases.SearchTopicsUseCase
import com.example.github.topic.mapper.TopicToTopicUiModelMapper
import com.example.github.topic.model.TopicUiModel
import com.example.github.topic.uiState.TopicsUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class TopicsViewModel(
    private val searchTopicsUseCase: SearchTopicsUseCase,
    private val topicToTopicUiModelMapper: TopicToTopicUiModelMapper,
) : ViewModel() {
    private val mutableTopicsUiState: MutableStateFlow<TopicsUiState> =
        MutableStateFlow(TopicsUiState.Initial)

    val topicsUiState: StateFlow<TopicsUiState> = mutableTopicsUiState.asStateFlow()

    private var mutableSearchQuery: MutableStateFlow<String> =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> = mutableSearchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        if (query.isEmpty()) mutableTopicsUiState.value = TopicsUiState.Initial
        mutableSearchQuery.value = query
    }

    fun isTopicsListEmpty(
        itemCount: Int,
        isDataInErrorState: Boolean,
    ) {
        if (itemCount == 0 && isDataInErrorState.not()) {
            mutableTopicsUiState.value =
                TopicsUiState.Empty
        }
    }

    fun searchTopics(name: String) {
        viewModelScope.launch {
            mutableTopicsUiState.value = TopicsUiState.Loading

            runCatching {
                searchTopicsUseCase(name = name)
            }.mapCatching { pagingTopics: Flow<PagingData<Topic>> ->

                topicToTopicUiModelMapper.mappingObjects(pagingTopics)
            }.mapCatching { topics: Flow<PagingData<TopicUiModel>> ->

                mutableTopicsUiState.value = TopicsUiState.Success(data = topics)
            }.onFailure { exception ->
                mutableTopicsUiState.value = TopicsUiState.Error.Unknown("Unknown error occurred")
                Timber.e("Unknown error occurred!")
            }
        }
    }
}
