package com.example.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.model.PullRequestResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.PullRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PullRequestResponseToPullRequestMapper(
    private val labelMapper: LabelResponseToLabelMapper,
    private val reviewerMapper: RequestedReviewerResponseToRequestedReviewerMapper,
) : Mapper<PullResponse, PullModel> {
    override suspend fun mappingObjects(input: PullResponse): PullModel =

        input.map { pagingRepository: PagingData<PullRequestResponse> ->

            pagingRepository.map { pullRequestResponse: PullRequestResponse ->

                PullRequest(
                    id = pullRequestResponse.id,
                    repositoryName = pullRequestResponse.baseResponse.repo.name,
                    ownerName = pullRequestResponse.baseResponse.repo.owner.authorName,
                    title = pullRequestResponse.title,
                    userImage = pullRequestResponse.user.avatarUrl,
                    userName = pullRequestResponse.user.name,
                    labels = labelMapper.mappingObjects(pullRequestResponse.labels),
                    requestedReviewers = reviewerMapper.mappingObjects(pullRequestResponse.requestedReviewers),
                )
            }
        }
}

private typealias PullResponse = Flow<PagingData<PullRequestResponse>>
private typealias PullModel = Flow<PagingData<PullRequest>>
