package com.idle.care

import android.app.Application
import com.idle.analytics.AnalyticsHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CareApplication : Application() {
    @Inject
    lateinit var amplitudeHelper: AnalyticsHelper
}