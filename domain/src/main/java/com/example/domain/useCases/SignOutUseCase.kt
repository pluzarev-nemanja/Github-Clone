package com.example.domain.useCases

import com.example.domain.repository.AuthRepository
import timber.log.Timber

class SignOutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        authRepository.runCatching {
            signOut()
        }.onFailure { exception ->
            Timber.e("Error in use case : $exception")
        }.getOrThrow()
    }
}
