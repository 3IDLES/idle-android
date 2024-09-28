package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class FCMTokenRequest(val deviceToken: String)