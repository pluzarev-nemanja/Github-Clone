package com.example.domain.useCases

import androidx.paging.PagingData
import com.example.domain.model.SearchedRepository
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class SearchRepositoriesUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(name: String): Flow<PagingData<SearchedRepository>> =
        searchRepository.runCatching {
            searchRepositories(name = name)
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
