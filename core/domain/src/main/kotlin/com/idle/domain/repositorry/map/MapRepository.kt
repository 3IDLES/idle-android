package com.idle.domain.repositorry.map

import com.idle.domain.model.map.MapMarker

interface MapRepository {
    suspend fun getStaticMap(
        width: Int,
        height: Int,
        markers: List<MapMarker>
    ): Result<ByteArray>
}