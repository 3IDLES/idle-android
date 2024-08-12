package com.idle.analytics

import com.amplitude.android.Amplitude
import javax.inject.Inject

class AnalyticsHelper @Inject constructor(
    private val amplitude: Amplitude,
) {
    fun logEvent(event: AnalyticsEvent) {
        amplitude.track(event)
    }
}