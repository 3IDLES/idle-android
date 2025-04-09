package com.idle.center.jobposting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.idle.analytics.businessmetric.AnalyticsEvent
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.idle.analytics.businessmetric.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.idle.analytics.businessmetric.AnalyticsEvent.Types.SCREEN_VIEW
import com.idle.analytics.businessmetric.AnalyticsHelper
import com.idle.analytics.businessmetric.LocalAnalyticsHelper

@Composable
internal fun LogJobPostingStep(
    step: JobPostingStep,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(true) {
    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = SCREEN_VIEW,
            properties = mutableMapOf(
                ACTION_NAME to "post_job_posting",
                SCREEN_NAME to "post_job_posting_" + step.name.lowercase(),
                "step" to step.step,
            )
        )
    )
}
