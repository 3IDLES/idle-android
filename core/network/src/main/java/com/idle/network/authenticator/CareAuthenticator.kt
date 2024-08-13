package com.idle.network.authenticator

import com.idle.domain.model.error.HttpResponseStatus
import com.idle.network.api.CareApi
import com.idle.network.di.TokenManager
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

class CareAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val careApi: Provider<CareApi>,
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

        if (response.code != HttpResponseStatus.Unauthorized.code) {
            return null
        }

        val token = runBlocking {
            careApi.get().refreshToken(RefreshTokenRequest(tokenManager.getRefreshToken()))
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