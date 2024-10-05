package com.example.domain.useCases

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import timber.log.Timber

class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userName: String): User =
        userRepository.runCatching {
            getUser(username = userName)
        }.onFailure {
            Timber.d("There is an error : $it")
        }.getOrThrow()
}
