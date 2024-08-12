package com.idle.analytics

import com.amplitude.core.events.BaseEvent

data class AnalyticsEvent(
    val type: String,
    val properties: MutableMap<String, Any?>? = null,
    val eventGroups: MutableMap<String, Any?>? = null,
    val id: String? = null,
) : BaseEvent() {
    init {
        this.eventType = type
        this.eventProperties = this.properties
        this.groups = eventGroups
        this.insertId = id
    }

    object Types {
        const val SCREEN_VIEW = "screen_view"
    }

    object PropertiesKeys {
        const val SCREEN_NAME = "screen_name"
    }
}