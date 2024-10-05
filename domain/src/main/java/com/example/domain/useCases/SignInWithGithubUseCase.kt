package com.example.domain.useCases

import com.example.domain.model.AuthUser
import com.example.domain.repository.AuthRepository
import timber.log.Timber

class SignInWithGithubUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(token: String): AuthUser? =
        authRepository.runCatching {
            signInWithGithub(token)
        }.onFailure { exception ->
            Timber.e("Error in use case : $exception")
        }.getOrThrow()
}
