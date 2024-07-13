package com.idle.network.token

interface TokenProvider {
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
}