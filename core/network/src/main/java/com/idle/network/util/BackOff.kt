package com.idle.network.util

import com.idle.domain.model.CountDownTimer.Companion.TICK_INTERVAL
import kotlin.math.pow
import kotlin.random.Random

const val MAX_RETRY_ATTEMPTS = 5
const val MAX_WAIT_TIME = 10_000L

fun calculateEqualJitter(attempt: Int): Long {
    val baseDelay = calculateBackoffTime(attempt)
    val randomJitter = Random.nextLong(0, baseDelay / 2 + 1)
    return baseDelay / 2 + randomJitter
}

private fun calculateBackoffTime(attempt: Int): Long = (2.0.pow(attempt) * TICK_INTERVAL).toLong()
