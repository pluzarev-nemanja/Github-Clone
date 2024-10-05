package com.example.data.repository

import android.os.Bundle
import com.example.domain.model.TrackingEvent
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class AnalyticsRepositoryImplTest {
    private val firebaseAnalytics = mockk<FirebaseAnalytics>(relaxed = true)
    private val analyticsRepositoryImpl = AnalyticsRepositoryImpl(firebaseAnalytics)
    private val bundle = mockk<Bundle>(relaxed = true)

    @Test
    fun `given TrackingEvent When logEvent function is called should execute function`() =
        runTest {
            val trackingEvent =
                TrackingEvent(
                    trackingEventName = "test_event",
                    data = bundle.apply { putString("key", "value") },
                )
            analyticsRepositoryImpl.logEvent(trackingEvent)

            coVerify { firebaseAnalytics.logEvent(any(), any()) }
        }

    @After
    fun teardown() {
        clearAllMocks()
    }
}
