package com.idle.withdrawal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.SCREEN_VIEW
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.analytics.helper.LocalAnalyticsHelper

@Composable
internal fun LogWithdrawalStep(
    step: WithdrawalStep,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(true) {
    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = SCREEN_VIEW,
            properties = mutableMapOf(
                ACTION_NAME to "withdrawal",
                SCREEN_NAME to "withdrawal_" + step.name.lowercase(),
                "step" to step.step,
            )
        )
    )
}