package com.idle.network.util

import retrofit2.Response

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        body()?.let {
            return Result.success(it)
        } ?: return Result.success(Unit as T)
    } else {
        val errorBody = errorBody()
        throw IllegalArgumentException(errorBody.toString())
    }
}