package com.idle.network.di

interface TokenManager {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    suspend fun setAccessToken(accessToken: String)
    suspend fun setRefreshToken(refreshToken: String)
}