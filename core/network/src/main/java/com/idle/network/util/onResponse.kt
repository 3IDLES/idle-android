package com.idle.network.util

import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import com.idle.network.model.error.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let {
            return Result.success(it)
        } ?: return Result.success(Unit as T)
    } else {
        errorBody()?.let {
            val errorResponse = Json.decodeFromString<ErrorResponse>(it.string())

            return Result.failure(
                HttpResponseException(
                    status = HttpResponseStatus.create(code()),
                    rawCode = errorResponse.code,
                    msg = errorResponse.message,
                )
            )
        } ?: return Result.failure(
            HttpResponseException(status = HttpResponseStatus.create(-1), rawCode = "UNKNOWN")
    }
}