package com.idle.center.jobposting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.AnalyticsEvent
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.AnalyticsEvent.Types.ACTION
import com.idle.analytics.helper.AnalyticsHelper
import com.idle.analytics.helper.LocalAnalyticsHelper

@Composable
internal fun LogJobPostingStep(
    step: JobPostingStep,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(true) {
    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = ACTION,
            properties = mutableMapOf(
                ACTION_NAME to "post_job_posting",
                SCREEN_NAME to "post_job_posting_" + step.name.lowercase(),
                "step" to step.step,
            )
        )
    )
}