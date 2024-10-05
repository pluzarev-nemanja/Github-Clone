package com.example.github.userRepo.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.ErrorResponse
import com.example.github.userRepo.uiState.CommitUiState

class ThrowableToCommitUiStateErrorMapper : Mapper<Throwable, CommitUiState.Error> {
    override suspend fun mappingObjects(input: Throwable): CommitUiState.Error =
        when (input) {
            is ErrorResponse.Network -> CommitUiState.Error.Connection(message = "Check your internet connection!")
            is ErrorResponse.Host -> CommitUiState.Error.Server(message = "Something went wrong on server.")
            else -> CommitUiState.Error.Unknown("Unknown error occurred.")
        }
}
