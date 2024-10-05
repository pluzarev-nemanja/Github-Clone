package com.example.domain.dataSource

import kotlinx.coroutines.flow.Flow

interface MessageTokenDataSource {
    suspend fun saveMessageToken()

    suspend fun readMessageToken(): Flow<String>
}
