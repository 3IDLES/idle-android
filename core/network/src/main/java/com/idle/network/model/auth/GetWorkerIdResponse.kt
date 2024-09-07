package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class GetWorkerIdResponse(
    val carerId: String,
)