package com.example.github.pullRequest.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.RequestedReviewer
import com.example.github.pullRequest.model.ReviewerUiModel

class ReviewerToReviewerUiModelMapper : Mapper<List<RequestedReviewer>, List<ReviewerUiModel>> {
    override suspend fun mappingObjects(input: List<RequestedReviewer>): List<ReviewerUiModel> =
        input.map { requestedReviewer: RequestedReviewer ->
            ReviewerUiModel(
                reviewerName = requestedReviewer.name,
                reviewerImage = requestedReviewer.avatarUrl,
            )
        }
}
