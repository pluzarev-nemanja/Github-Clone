package com.example.domain.useCases

import com.example.domain.model.RepositoryDetails
import com.example.domain.repository.ReposRepository
import timber.log.Timber

class GetRepositoryDetailsUseCase(
    private val reposRepository: ReposRepository,
) {
    suspend operator fun invoke(
        owner: String,
        repository: String,
    ): RepositoryDetails =
        reposRepository.runCatching {
            getRepositoryDetails(owner = owner, repository = repository)
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
