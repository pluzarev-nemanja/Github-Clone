package com.example.github.userRepo.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.ErrorResponse
import com.example.github.userRepo.uiState.RepositoryDetailsUiState

class ThrowableToRepositoryDetailsUiStateErrorMapper :
    Mapper<Throwable, RepositoryDetailsUiState.Error> {
    override suspend fun mappingObjects(input: Throwable): RepositoryDetailsUiState.Error =
        when (input) {
            is ErrorResponse.Network -> RepositoryDetailsUiState.Error.Connection(message = "Check your internet connection!")
            is ErrorResponse.Host -> RepositoryDetailsUiState.Error.Server(message = "Something went wrong on server.")
            else -> RepositoryDetailsUiState.Error.Unknown("Unknown error occurred.")
        }
}
