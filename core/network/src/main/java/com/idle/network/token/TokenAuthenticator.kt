package com.idle.network.token

import com.idle.network.api.TokenNetworkApi
import com.idle.network.model.token.RefreshTokenRequest
import com.idle.network.util.onResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenNetworkApi: TokenNetworkApi,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request

        if (originRequest.header("Authorization").isNullOrEmpty()) {
            return null
        }

        if (response.code != 401) {
            return null
        }

        val token = runBlocking {
            tokenNetworkApi.refreshToken(RefreshTokenRequest(tokenManager.getRefreshToken()))
                .onResponse()
        }.getOrNull()

        if (token == null) {
            return null
        }

        runBlocking {
            tokenManager.setAccessToken(token.accessToken)
            launch { tokenManager.setRefreshToken(token.refreshToken) }
        }

        return response.request
            .newBuilder()
            .header("Authorization", tokenManager.getAccessToken())
            .build()
    }
}