package com.idle.network.model.error

import com.idle.network.util.SEOUL_ZONE
import com.idle.network.util.UN_KNOWN
import java.time.ZoneId
import java.time.ZonedDateTime

internal data class ErrorResponse(
    val code: String = UN_KNOWN,
    val message: String = UN_KNOWN,
    val timestamp: String = ZonedDateTime.now(ZoneId.of(SEOUL_ZONE)).toString(),
)