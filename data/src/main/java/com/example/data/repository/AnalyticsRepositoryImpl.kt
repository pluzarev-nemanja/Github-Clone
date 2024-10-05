package com.example.data.repository

import com.example.domain.model.TrackingEvent
import com.example.domain.repository.AnalyticsRepository
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsRepositoryImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsRepository {
    override suspend fun logEvent(trackingEvent: TrackingEvent) {
        firebaseAnalytics.logEvent(trackingEvent.trackingEventName, trackingEvent.data)
    }
}
