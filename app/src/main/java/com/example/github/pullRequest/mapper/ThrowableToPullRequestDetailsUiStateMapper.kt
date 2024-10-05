package com.example.github.pullRequest.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.ErrorResponse
import com.example.github.pullRequest.uiState.PullRequestDetailsUiState

class ThrowableToPullRequestDetailsUiStateMapper :
    Mapper<Throwable, PullRequestDetailsUiState.Error> {
    override suspend fun mappingObjects(input: Throwable): PullRequestDetailsUiState.Error =
        when (input) {
            is ErrorResponse.Network -> PullRequestDetailsUiState.Error.Connection(message = "Check your internet connection!")
            is ErrorResponse.Host -> PullRequestDetailsUiState.Error.Server(message = "Something went wrong on server.")
            else -> PullRequestDetailsUiState.Error.Unknown("Unknown error occurred.")
        }
}
