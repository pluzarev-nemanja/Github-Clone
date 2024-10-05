package com.example.domain.useCases

import com.example.domain.model.PullRequestDetails
import com.example.domain.repository.PullRequestRepository
import timber.log.Timber

class GetPullRequestDetailsUseCase(
    private val pullRequestRepository: PullRequestRepository,
) {
    suspend operator fun invoke(
        owner: String,
        repository: String,
        pullNumber: Int,
    ): PullRequestDetails =
        pullRequestRepository.runCatching {
            getPullRequestDetails(
                owner = owner,
                repository = repository,
                pullNumber = pullNumber,
            )
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
