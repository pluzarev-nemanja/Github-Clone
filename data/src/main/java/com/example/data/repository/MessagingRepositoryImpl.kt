package com.example.data.repository

import com.example.domain.dataSource.MessageTokenDataSource
import com.example.domain.repository.MessagingRepository
import kotlinx.coroutines.flow.Flow

class MessagingRepositoryImpl(
    private val messageTokenDataSource: MessageTokenDataSource,
) : MessagingRepository {
    override suspend fun saveMessageToken() {
        messageTokenDataSource.saveMessageToken()
    }

    override suspend fun readMessageToken(): Flow<String> = messageTokenDataSource.readMessageToken()
}
