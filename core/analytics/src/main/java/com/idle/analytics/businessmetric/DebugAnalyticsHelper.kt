package com.idle.analytics.businessmetric

import android.util.Log
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor(
    private var userId: String = "",
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d("DebugAnalyticsHelper", "${this.userId}, $event")
    }

    override fun setUserId(userId: String?) {
        this.userId = userId ?: ""
        Log.d("DebugAnalyticsHelper", "setUserId 호출 : ${this.userId}")
    }
}
