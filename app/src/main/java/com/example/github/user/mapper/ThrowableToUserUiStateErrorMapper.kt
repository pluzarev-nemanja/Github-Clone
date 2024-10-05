package com.example.github.user.mapper

import com.example.domain.mapper.Mapper
import com.example.domain.model.ErrorResponse
import com.example.github.user.uiState.UserUiState

class ThrowableToUserUiStateErrorMapper : Mapper<Throwable, UserUiState.Error> {
    override suspend fun mappingObjects(input: Throwable): UserUiState.Error =
        when (input) {
            is ErrorResponse.Network -> UserUiState.Error.Connection(message = "Check your internet connection!")
            is ErrorResponse.Host -> UserUiState.Error.Server(message = "Something went wrong on server.")
            else -> UserUiState.Error.Unknown("Unknown error occurred.")
        }
}
