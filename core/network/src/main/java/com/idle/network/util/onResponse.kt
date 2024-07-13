package com.idle.network.util

import retrofit2.Response

internal fun <T> Response<T>.onResponse(): Result<T> {
    if (isSuccessful) {
        return Result.success(body()!!)
    } else {
        val errorBody = errorBody()
        throw IllegalArgumentException(errorBody.toString())
    }
}