package com.idle.analytics.businessmetric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.ACTION_RESULT
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.BUTTON_ID
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.DURATION
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.businessmetric.AnalyticsEvent.Types.ACTION
import com.idle.analytics.businessmetric.AnalyticsEvent.Types.BUTTON_CLICK
import com.idle.analytics.businessmetric.AnalyticsEvent.Types.SCREEN_VIEW

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

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = LaunchedEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
}

data class AnalyticsEvent(
    val type: String,
    val properties: MutableMap<String, Any?>? = null,
) {
    object Types {
        const val SCREEN_VIEW = "screen_view"
        const val BUTTON_CLICK = "button_click"
        const val ACTION = "action"
    }

    object PropertiesKeys {
        const val SCREEN_NAME = "screen_name"
        const val ACTION_NAME = "action_name"
        const val ACTION_RESULT = "action_result"
        const val BUTTON_ID = "button_id"
        const val DURATION = "duration"
    }
}
