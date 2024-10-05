package com.example.domain.useCases

import androidx.paging.PagingData
import com.example.domain.model.Topic
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class SearchTopicsUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(name: String): Flow<PagingData<Topic>> =
        searchRepository.runCatching {
            searchTopics(name = name)
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
