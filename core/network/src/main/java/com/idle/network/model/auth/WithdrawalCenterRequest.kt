package com.idle.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class WithdrawalCenterRequest(
    val reason: String,
    val password: String,
)