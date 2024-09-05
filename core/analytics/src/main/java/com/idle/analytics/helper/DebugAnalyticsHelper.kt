package com.idle.analytics.helper

import android.util.Log
import com.idle.analytics.AnalyticsEvent
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor(
    private var userId: String = "",
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d("DebugAnalyticsHelper", "$userId, $event")
    }

    override fun setUserId(userId: String) {
        this.userId = userId
    }
}