package com.idle.analytics.businessmetric

import com.idle.analytics.AnalyticsEvent

class NoOpAnalyticsHelper : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
}