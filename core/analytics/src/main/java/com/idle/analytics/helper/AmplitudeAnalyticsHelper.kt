package com.idle.analytics.helper

import com.amplitude.android.Amplitude
import com.amplitude.core.events.BaseEvent
import com.idle.analytics.AnalyticsEvent
import javax.inject.Inject

class AmplitudeAnalyticsHelper @Inject constructor(
    private val amplitude: Amplitude,
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        amplitude.track(event = event.toAmplitudeEvent())
    }

    override fun setUserId(userId: String) {
        amplitude.setUserId(userId)
    }

    private fun AnalyticsEvent.toAmplitudeEvent(): BaseEvent = BaseEvent().apply {
        this.eventType = type
        this.eventProperties = properties
    }
}