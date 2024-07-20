package com.idle.network.token

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = if (isAccessTokenUsed(originRequest)) {
            originRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${tokenManager.getAccessToken()}")
                .build()
        } else {
            originRequest
        }
        return chain.proceed(newRequest)
    }

    private fun isAccessTokenUsed(request: Request): Boolean {
        if (request.url.encodedPath.contains("/api/v1/auth/center/validation/")) {
            return false
        }

        if (request.url.encodedPath.contains("/api/v1/auth/center/authentication/")) {
            return false
        }

        return when (request.url.encodedPath) {
            "/api/v1/auth/common/send" -> false
            "/api/v1/auth/common/confirm" -> false
            "/api/v1/auth/center/join" -> false
            "/api/v1/auth/center/login" -> false
            else -> true
        }
    }
}