package com.idle.network.source.map

import com.idle.domain.model.map.MapMarker
import com.idle.network.api.NaverApi
import com.idle.network.model.map.DataVersionResponse
import com.idle.network.util.onResponse
import javax.inject.Inject

class MapDataSource @Inject constructor(
    private val naverApi: NaverApi,
) {
    suspend fun getStaticMap(
        width: Int,
        height: Int,
        markers: List<MapMarker>,
        dataVersion: String
    ): Result<ByteArray> = naverApi.getStaticMap(
        width = width,
        height = height,
        markers = markers.map { it.toString() },
        dataVersion = dataVersion,
    )
        .onResponse()
        .mapCatching { it.bytes() }

    suspend fun updateMapDataVersion(): Result<DataVersionResponse> =
        naverApi.getMapDataVersion().onResponse()
}