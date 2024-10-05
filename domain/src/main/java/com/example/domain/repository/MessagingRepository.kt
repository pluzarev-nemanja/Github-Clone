package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    suspend fun saveMessageToken()

    suspend fun readMessageToken(): Flow<String>
}
