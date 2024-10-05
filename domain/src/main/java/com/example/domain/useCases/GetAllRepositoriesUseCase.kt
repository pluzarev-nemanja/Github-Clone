package com.example.domain.useCases

import androidx.paging.PagingData
import com.example.domain.model.Repository
import com.example.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetAllRepositoriesUseCase(
    private val reposRepository: ReposRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<Repository>> =
        reposRepository.runCatching {
            getAllRepositories()
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
