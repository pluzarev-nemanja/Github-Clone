package com.example.domain.repository

import com.example.domain.model.TrackingEvent

interface AnalyticsRepository {
    suspend fun logEvent(trackingEvent: TrackingEvent)
}
