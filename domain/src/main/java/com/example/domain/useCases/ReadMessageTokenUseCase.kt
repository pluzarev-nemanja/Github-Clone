package com.example.domain.useCases

import com.example.domain.repository.MessagingRepository
import kotlinx.coroutines.flow.Flow

class ReadMessageTokenUseCase(
    private val messagingRepository: MessagingRepository,
) {
    suspend operator fun invoke(): Flow<String> = messagingRepository.readMessageToken()
}
