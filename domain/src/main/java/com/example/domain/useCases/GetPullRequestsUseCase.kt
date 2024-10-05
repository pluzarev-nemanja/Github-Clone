package com.example.domain.useCases

import androidx.paging.PagingData
import com.example.domain.model.PullRequest
import com.example.domain.repository.PullRequestRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class GetPullRequestsUseCase(
    private val pullRequestRepository: PullRequestRepository,
) {
    suspend operator fun invoke(
        owner: String,
        repository: String,
    ): Flow<PagingData<PullRequest>> =
        pullRequestRepository.runCatching {
            getPullRequests(
                owner = owner,
                repository = repository,
            )
        }.onFailure {
            Timber.d("There is an error")
        }.getOrThrow()
}
