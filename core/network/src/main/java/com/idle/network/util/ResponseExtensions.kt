package com.idle.network.util

import com.idle.domain.model.error.ApiErrorCode
import com.idle.domain.model.error.HttpResponseException
import com.idle.domain.model.error.HttpResponseStatus
import com.idle.network.model.error.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

private val json = Json { ignoreUnknownKeys = true }

internal fun <T> Response<T>.onResponse(): T {
    if (isSuccessful) {
        return body() ?: Unit as T
    } else {
        errorBody()?.let {
            val errorResponse = json.decodeFromString<ErrorResponse>(it.string())

            throw HttpResponseException(
                status = HttpResponseStatus.create(code()),
                apiErrorCode = ApiErrorCode.create(errorResponse.code),
                msg = errorResponse.message,
            )
        } ?: throw HttpResponseException(
            status = HttpResponseStatus.create(-1),
            apiErrorCode = ApiErrorCode.UnknownError,
            msg = "알 수 없는 에러입니다."
        )
    }
}
