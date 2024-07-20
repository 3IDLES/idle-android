package com.idle.network.model.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: String = "-1",
    val message: String = "UNKNOWN",
    val timestamp: String = "",
)