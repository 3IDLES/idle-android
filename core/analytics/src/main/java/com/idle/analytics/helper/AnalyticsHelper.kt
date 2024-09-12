package com.idle.analytics.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_RESULT
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.BUTTON_ID
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.DURATION
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.ACTION
import com.idle.analytics.AnalyticsEvent.Types.BUTTON_CLICK
import com.idle.analytics.AnalyticsEvent.Types.SCREEN_VIEW

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
    abstract fun setUserId(userId: String?)

    // XML로 구성된 화면이 존재하기 때문에 public으로 냅둠
    fun logScreenView(screenName: String) {
        logEvent(
            AnalyticsEvent(
                type = SCREEN_VIEW,
                properties = mutableMapOf(
                    SCREEN_NAME to screenName,
                ),
            ),
        )
    }

    fun logButtonClick(
        screenName: String,
        buttonId: String,
        properties: MutableMap<String, Any?>? = null,
    ) {
        val eventProperties = mutableMapOf<String, Any?>(
            SCREEN_NAME to screenName,
            BUTTON_ID to buttonId,
        )

        properties?.let { eventProperties.putAll(it) }

        logEvent(
            AnalyticsEvent(
                type = BUTTON_CLICK,
                properties = eventProperties
            )
        )
    }

    fun logActionDuration(
        screenName: String,
        actionName: String,
        isSuccess: Boolean,
        timeMillis: Long,
    ) = logEvent(
        AnalyticsEvent(
            type = ACTION,
            properties = mutableMapOf(
                SCREEN_NAME to screenName,
                ACTION_NAME to actionName,
                ACTION_RESULT to isSuccess,
                DURATION to timeMillis,
            )
        )
    )
}

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = LaunchedEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
}