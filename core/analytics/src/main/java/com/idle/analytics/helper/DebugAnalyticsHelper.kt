package com.idle.analytics.helper

import android.util.Log
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.TIMESTAMP
import java.text.SimpleDateFormat
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor(
    private var userId: String = "",
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timestamp = dateFormat.format(now)
        event.properties?.put(key = TIMESTAMP, value = timestamp)
        Log.d("DebugAnalyticsHelper", "$userId, $event")
    }

    override fun setUserId(userId: String) {
        this.userId = userId
    }
}
