package com.idle.network.util

import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import kotlin.math.pow

const val MAX_RETRY_ATTEMPTS = 5
const val MAX_WAIT_TIME = 10_000L

fun calculateBackoffTime(attempt: Int): Long = (2.0.pow(attempt) * TICK_INTERVAL).toLong()
