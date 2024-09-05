package com.idle.analytics.helper

import com.amplitude.android.Amplitude
import com.amplitude.core.events.BaseEvent
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.TIMESTAMP
import java.text.SimpleDateFormat
import javax.inject.Inject

class AmplitudeAnalyticsHelper @Inject constructor(
    private val amplitude: Amplitude,
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timestamp = dateFormat.format(now)
        event.properties?.put(key = TIMESTAMP, value = timestamp)
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