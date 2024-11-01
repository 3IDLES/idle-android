package com.idle.analytics.error

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsErrorLoggingHelper @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : ErrorLoggingHelper {
    override fun logError(exception: Throwable) {
        firebaseCrashlytics.recordException(exception)
    }
}