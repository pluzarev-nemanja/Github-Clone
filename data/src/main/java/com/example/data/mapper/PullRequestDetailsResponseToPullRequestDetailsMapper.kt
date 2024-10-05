package com.example.data.mapper

import com.example.data.model.PullRequestDetailsResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.PullRequestDetails

class PullRequestDetailsResponseToPullRequestDetailsMapper(
    private val labelMapper: LabelResponseToLabelMapper,
    private val reviewerMapper: RequestedReviewerResponseToRequestedReviewerMapper,
    private val milestoneMapper: MilestoneResponseToMilestoneMapper,
) : Mapper<PullRequestDetailsResponse, PullRequestDetails> {
    override suspend fun mappingObjects(input: PullRequestDetailsResponse): PullRequestDetails =
        PullRequestDetails(
            userName = input.user.name,
            userImage = input.user.avatarUrl,
            title = input.title,
            description = input.body,
            labels = labelMapper.mappingObjects(input.labels),
            milestone = milestoneMapper.mappingObjects(input.milestone),
            requestedReviewers = reviewerMapper.mappingObjects(input.requestedReviewers),
        )
}
