package com.idle.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.SCREEN_VIEW
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.analytics.helper.LocalAnalyticsHelper
import com.idle.signup.center.CenterSignUpStep
import com.idle.signin.worker.WorkerSignUpStep

@Composable
internal fun LogCenterSignUpStep(
    step: CenterSignUpStep,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(true) {
    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = SCREEN_VIEW,
            properties = mutableMapOf(
                ACTION_NAME to "center_signup",
                SCREEN_NAME to "center_signup_" + step.name.lowercase(),
                "step" to step.step + 1,
            )
        )
    )
}

@Composable
internal fun LogWorkerSignUpStep(
    step: WorkerSignUpStep,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(true) {
    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = SCREEN_VIEW,
            properties = mutableMapOf(
                ACTION_NAME to "carer_signup",
                SCREEN_NAME to "carer_signup_" + step.name.lowercase(),
                "step" to step.step + 1,
            )
        )
    )
}