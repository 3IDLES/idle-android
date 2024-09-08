package com.idle.network.util

import retrofit2.Response

internal suspend inline fun <T> safeApiCall(apiCall: () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        response.onResponse()
    } catch (e: Exception) {
        Result.failure(e)
    }
}