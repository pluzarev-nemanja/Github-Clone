package com.example.data.mapper

import com.example.data.model.RequestedReviewerResponse
import com.example.domain.mapper.Mapper
import com.example.domain.model.RequestedReviewer

class RequestedReviewerResponseToRequestedReviewerMapper :
    Mapper<List<RequestedReviewerResponse>, List<RequestedReviewer>> {
    override suspend fun mappingObjects(input: List<RequestedReviewerResponse>): List<RequestedReviewer> =
        input.map { requestedReviewer: RequestedReviewerResponse ->
            RequestedReviewer(
                avatarUrl = requestedReviewer.avatarUrl,
                name = requestedReviewer.name,
            )
        }
}
