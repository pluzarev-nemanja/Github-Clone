package com.example.domain.useCases

import com.example.domain.repository.ReposRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetRepositoryCommitsUseCase(
    private val reposRepository: ReposRepository,
) {
    suspend operator fun invoke(
        owner: String,
        repository: String,
    ): Flow<List<Int>> =
        runCatching {
            reposRepository.getRepositoryCommits(
                owner = owner,
                repository = repository,
            )
        }.onFailure {
            Timber.e("Error occurred!")
        }.getOrThrow()
}
