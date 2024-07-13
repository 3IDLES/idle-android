package com.idle.network.token

interface TokenProvider {
    fun getAccessToken(): String
    fun getRefreshToken(): String
}