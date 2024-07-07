package com.idle.network.source

import com.idle.network.di.CareNetworkApi
import com.idle.network.model.auth.AuthRequest
import com.idle.network.util.onResponse
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi
) {
    suspend fun sendAuthNumber(phoneNumber: String): Result<Unit> =
        careNetworkApi.sendAuthNumber(AuthRequest(phoneNumber)).onResponse()
}