package com.idle.network.util

import retrofit2.Response

fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let { body ->
            return Result.success(body)
        } ?: return Result.failure(Throwable("Response Body is Null"))
    } else {
        return Result.failure(errorBody()?.string()?.let { Throwable(it) }
            ?: Throwable("Response is Failure"))
    }
}