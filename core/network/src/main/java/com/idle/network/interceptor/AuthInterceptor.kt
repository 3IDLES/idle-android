package com.idle.network.interceptor

import com.idle.network.BuildConfig
import com.idle.network.di.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()

        // NAVER API 키를 추가해야 하는 경우
        if (isAccessTokenUsed(originRequest) && originRequest.url.host == BuildConfig.NAVER_BASE_URL) {
            requestBuilder
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_CLIENT_ID)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_CLIENT_SECRET)
        }

        // 일반 Bearer 토큰을 추가해야 하는 경우
        else if (isAccessTokenUsed(originRequest)) {
            requestBuilder.addHeader("Authorization", "Bearer ${tokenManager.getAccessToken()}")
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun isAccessTokenUsed(request: Request): Boolean {
        if (request.url.encodedPath.contains("/api/v1/auth/center/validation/")) {
            return false
        }

        if (request.url.encodedPath.contains("/api/v1/auth/center/authentication/")) {
            return false
        }

        if (request.url.host.contains("idle-bucket.s3.ap-northeast-2.amazonaws.com")) {
            return false
        }

        return when (request.url.encodedPath) {
            "/api/v1/auth/common/send" -> false
            "/api/v1/auth/common/confirm" -> false
            "/api/v1/auth/center/join" -> false
            "/api/v1/auth/center/login" -> false
            "/api/v1/auth/center/refresh" -> false
            "/api/v1/auth/carer/join" -> false
            else -> true
        }
    }
}