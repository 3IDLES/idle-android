package com.idle.network.util

import android.util.Log
import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import com.idle.network.model.error.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

internal inline fun <T> safeApiCall(apiCall: () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        response.onResponse()
    } catch (e: Exception) {
        Log.d("test", e.stackTraceToString())

        Result.failure(
            HttpResponseException(
                status = HttpResponseStatus.create(-1),
                apiErrorCode = ApiErrorCode.NetworkError
            )
        )
    }
}

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let {
            return Result.success(it)
        } ?: return Result.success(Unit as T)
    } else {
        errorBody()?.let {
            val json = Json { ignoreUnknownKeys = true }
            val errorResponse = json.decodeFromString<ErrorResponse>(it.string())

            return Result.failure(
                HttpResponseException(
                    status = HttpResponseStatus.create(code()),
                    apiErrorCode = ApiErrorCode.create(errorResponse.code),
                    msg = errorResponse.message,
                )
            )
        } ?: return Result.failure(
            HttpResponseException(
                status = HttpResponseStatus.create(-1),
                apiErrorCode = ApiErrorCode.UnknownError
            )
        )
    }
}