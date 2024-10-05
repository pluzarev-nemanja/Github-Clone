package com.example.domain.useCases

import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository
import timber.log.Timber

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): AuthUser? =
        authRepository.runCatching {
            getCurrentUser()
        }.onFailure { exception ->
            Timber.e("Error in use case : $exception")
        }.getOrThrow()
}
