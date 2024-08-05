package com.idle.network.token

import com.idle.network.api.CareNetworkApi
import com.idle.network.model.token.RefreshTokenRequest
import com.idle.network.util.onResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val careNetworkApi: Provider<CareNetworkApi>,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request

        if (originRequest.header("Authorization").isNullOrEmpty()) {
            return null
        }

        val retryCount = originRequest.header(RETRY_HEADER)?.toIntOrNull() ?: 0
        if (retryCount >= MAX_RETRY_COUNT) {
            return null
        }

        if (response.code != 401) {
            return null
        }

        val token = runBlocking {
            careNetworkApi.get().refreshToken(RefreshTokenRequest(tokenManager.getRefreshToken()))
                .onResponse()
        }.getOrNull()

        if (token == null) {
            return null
        }

        runBlocking {
            tokenManager.setAccessToken(token.accessToken)
            launch { tokenManager.setRefreshToken(token.refreshToken) }
        }

        val newRequest = response.request
            .newBuilder()
            .header("Authorization", "Bearer ${token.accessToken}")
            .header(RETRY_HEADER, (retryCount + 1).toString())
            .build()

        return newRequest
    }

    companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val RETRY_HEADER = "Retry-Count"
    }
}