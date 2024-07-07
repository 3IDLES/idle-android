package com.idle.network.source

import com.idle.network.di.CareNetworkApi
import com.idle.network.model.auth.ConfirmAuthCodeRequest
import com.idle.network.model.auth.SendPhoneRequest
import com.idle.network.model.auth.SignUpCenterRequest
import com.idle.network.util.onResponse
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val careNetworkApi: CareNetworkApi
) {
    suspend fun sendPhoneNumber(sendPhoneRequest: SendPhoneRequest): Result<Unit> =
        careNetworkApi.sendPhoneNumber(sendPhoneRequest).onResponse()

    suspend fun confirmAuthCode(confirmAuthCodeRequest: ConfirmAuthCodeRequest): Result<Unit> =
        careNetworkApi.confirmAuthCode(confirmAuthCodeRequest).onResponse()

    suspend fun signUpCenter(signUpCenterRequest: SignUpCenterRequest): Result<Unit> =
        careNetworkApi.signUpCenter(signUpCenterRequest).onResponse()
}