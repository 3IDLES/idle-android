package com.idle.network.token

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authRequest = if (isAccessTokenUsed(request)) {
            request.newBuilder()
                .addHeader("Authorization", tokenProvider.getAccessToken())
                .build()
        } else {
            request
        }
        return chain.proceed(authRequest)
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
            "/api/v1/auth/center/refresh" -> false
            else -> true
        }
    }
}