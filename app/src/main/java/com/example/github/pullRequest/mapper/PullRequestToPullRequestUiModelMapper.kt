package com.example.github.pullRequest.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.mapper.Mapper
import com.example.domain.model.Label
import com.example.domain.model.PullRequest
import com.example.domain.model.RequestedReviewer
import com.example.github.pullRequest.model.PullRequestUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class PullRequestToPullRequestUiModelMapper(
    private val labelToLabelUiModelMapper: LabelToLabelUiModelMapper,
    private val reviewerToReviewerUiModelMapper: ReviewerToReviewerUiModelMapper,
) : Mapper<Flow<PagingData<PullRequest>>, Flow<PagingData<PullRequestUiModel>>> {
    override suspend fun mappingObjects(input: Flow<PagingData<PullRequest>>): Flow<PagingData<PullRequestUiModel>> =
        input.map { paggingData: PagingData<PullRequest> ->
            paggingData.map { pullRequest: PullRequest ->
                PullRequestUiModel(
                    id = pullRequest.id,
                    repositoryName = pullRequest.repositoryName,
                    ownerName = pullRequest.ownerName,
                    title = pullRequest.title,
                    userName = pullRequest.userName,
                    userImage = pullRequest.userImage,
                    labels = pullRequest.labels.mappingLabels(),
                    reviewers = pullRequest.requestedReviewers.mappingReviewers(),
                )
            }
        }

    private suspend fun List<RequestedReviewer>.mappingReviewers() =
        runCatching {
            reviewerToReviewerUiModelMapper.mappingObjects(this)
        }.onFailure {
            Timber.e("Error occurred! $it")
        }.getOrThrow()

    private suspend fun List<Label>.mappingLabels() =
        runCatching {
            labelToLabelUiModelMapper.mappingObjects(this)
        }.onFailure {
            Timber.e("Error occurred! $it")
        }.getOrThrow()
}
