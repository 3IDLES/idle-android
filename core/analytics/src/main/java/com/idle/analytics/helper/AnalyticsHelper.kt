package com.idle.analytics.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.BUTTON_ID
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.TIMESTAMP
import com.idle.analytics.AnalyticsEvent.Types.BUTTON_CLICK
import com.idle.analytics.AnalyticsEvent.Types.SCREEN_VIEW
import java.text.SimpleDateFormat

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
    abstract fun setUserId(userId: String)

    // XML로 구성된 화면이 존재하기 때문에 public으로 냅둠
    fun logScreenView(screenName: String) {
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timestamp = dateFormat.format(now)

        logEvent(
            AnalyticsEvent(
                type = SCREEN_VIEW,
                properties = mutableMapOf(
                    SCREEN_NAME to screenName,
                    TIMESTAMP to timestamp
                ),
            ),
        )
    }

    fun logButtonClick(screenName: String, buttonId: String) {
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timestamp = dateFormat.format(now)

        logEvent(
            AnalyticsEvent(
                type = BUTTON_CLICK,
                properties = mutableMapOf(
                    SCREEN_NAME to screenName,
                    BUTTON_ID to buttonId,
                    TIMESTAMP to timestamp,
                )
            ),
        )
    }

    @Composable
    fun TrackScreenViewEvent(
        screenName: String,
    ) = LaunchedEffect(Unit) {
        logScreenView(screenName)
    }
}