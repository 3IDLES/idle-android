package com.idle.network.model.map

import kotlinx.serialization.Serializable

@Serializable
data class DataVersionResponse(
    val version: String,
    val interval: Int,
)
