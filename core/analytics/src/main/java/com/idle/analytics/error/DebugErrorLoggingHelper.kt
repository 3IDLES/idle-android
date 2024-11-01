package com.idle.analytics.error

import android.util.Log
import javax.inject.Inject

class DebugErrorLoggingHelper @Inject constructor() : ErrorLoggingHelper {
    override fun logError(exception: Throwable) {
        Log.e("DebugErrorLoggingHelper", exception.stackTraceToString())
    }
}