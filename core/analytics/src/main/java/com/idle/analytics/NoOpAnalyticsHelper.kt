package com.idle.analytics

class NoOpAnalyticsHelper : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
}
