package com.example.domain.useCases

import androidx.paging.PagingData
import com.example.domain.model.Repository
import com.example.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAuthRepositoriesUseCase(
    private val reposRepository: ReposRepository,
) {
    suspend operator fun invoke(token: String): Flow<PagingData<Repository>> =
        reposRepository.runCatching {
            getAuthRepositories(token = token)
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
