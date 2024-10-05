package com.example.domain.useCases

import com.example.domain.repository.MessagingRepository

class SaveMessageTokenUseCase(
    private val messagingRepository: MessagingRepository,
) {
    suspend operator fun invoke() {
        messagingRepository.saveMessageToken()
    }
}
