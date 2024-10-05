package com.example.domain.useCases

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import timber.log.Timber

class GetAuthenticatedUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(token: String): User =
        userRepository.runCatching {
            getAuthenticatedUser(token)
        }.onFailure { exception ->
            Timber.e("Error in use case : $exception")
        }.getOrThrow()
}
