package com.idle.network.util

import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import retrofit2.Response

internal inline fun <T> safeApiCall(apiCall: () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        response.onResponse()
    } catch (e: Exception) {
        Result.failure(
            HttpResponseException(
                status = HttpResponseStatus.create(-1),
                apiErrorCode = ApiErrorCode.NetworkError
            )
        )
    }
}