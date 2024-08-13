package com.idle.domain.repositorry.map

interface MapRepository {
    suspend fun getStaticMap(
        center: String,
        level: Int,
        width: Int,
        height: Int,
    ): Result<ByteArray>
}