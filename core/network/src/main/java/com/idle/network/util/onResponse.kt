package com.idle.network.util

import com.idle.network.model.error.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let { body ->
            return Result.success(body)
        } ?: return Result.failure(Throwable("Response Body is Null"))
    } else {
        val errorResponse = errorBody()?.string()?.let { errorBodyString ->
            try {
                Json.decodeFromString<ErrorResponse>(errorBodyString)
            } catch (e: Exception) {
                ErrorResponse()
            }
        } ?: ErrorResponse()

        return Result.failure(Throwable(errorResponse.message))
    }
}