package com.idle.analytics

data class AnalyticsEvent(
    val type: String,
    val properties: MutableMap<String, Any?>? = null,
) {
    object Types {
        const val SCREEN_VIEW = "screen_view"
        const val BUTTON_CLICK = "button_click"
    }

    object PropertiesKeys {
        const val SCREEN_NAME = "screen_name"
        const val ACTION_NAME ="action_name"
        const val BUTTON_ID = "button_id"
        const val TIMESTAMP = "time_stamp"
    }
}