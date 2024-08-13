package com.idle.network.source.map

import com.idle.network.api.NaverApi
import com.idle.network.model.map.DataVersionResponse
import com.idle.network.util.onResponse
import javax.inject.Inject

class MapDataSource @Inject constructor(
    private val naverApi: NaverApi,
) {
    suspend fun getStaticMap(
        center: String,
        level: Int,
        width: Int,
        height: Int,
        dataVersion: String
    ): Result<ByteArray> = naverApi.getStaticMap(
        center = center,
        level = level,
        width = width,
        height = height,
        dataVersion = dataVersion,
    )
        .onResponse()
        .mapCatching { it.bytes() }

    suspend fun updateMapDataVersion(): Result<DataVersionResponse> =
        naverApi.getMapDataVersion().onResponse()
}