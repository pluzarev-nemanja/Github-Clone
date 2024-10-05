package com.example.domain.useCases

import com.example.domain.model.TrackingEvent
import com.example.domain.repository.AnalyticsRepository
import timber.log.Timber

class LogEventUseCase(
    private val analyticsRepository: AnalyticsRepository,
) {
    suspend operator fun invoke(trackingEvent: TrackingEvent) {
        analyticsRepository.runCatching {
            logEvent(trackingEvent = trackingEvent)
        }.onFailure {
            Timber.e("Error occurred.")
        }.getOrThrow()
    }
}
