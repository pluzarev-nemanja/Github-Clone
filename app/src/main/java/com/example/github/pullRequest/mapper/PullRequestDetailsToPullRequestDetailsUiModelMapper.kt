package com.example.github.pullRequest.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.Label
import com.example.domain.model.PullRequestDetails
import com.example.domain.model.RequestedReviewer
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import timber.log.Timber

class PullRequestDetailsToPullRequestDetailsUiModelMapper(
    private val labelToLabelUiModelMapper: LabelToLabelUiModelMapper,
    private val reviewerToReviewerUiModelMapper: ReviewerToReviewerUiModelMapper,
) : Mapper<PullRequestDetails, PullRequestDetailsUiModel> {
    override suspend fun mappingObjects(input: PullRequestDetails): PullRequestDetailsUiModel =
        PullRequestDetailsUiModel(
            authorName = input.userName,
            authorImage = input.userImage,
            description = input.description ?: "No description found.",
            title = "~ ".plus(input.title).plus(" ~"),
            labels = input.labels.mappingLabels(),
            reviewers = input.requestedReviewers.mappingReviewers(),
            milestone = input.milestone ?: "No milestone found.",
        )

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
