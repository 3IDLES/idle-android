package com.idle.data.repository.map

import android.util.Log
import com.idle.datastore.datasource.MapDataVersionDataSource
import com.idle.domain.model.map.MapMarker
import com.idle.domain.repositorry.map.MapRepository
import com.idle.network.source.map.MapDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val mapDataVersionDataSource: MapDataVersionDataSource,
) : MapRepository {
    override suspend fun getStaticMap(
        width: Int,
        height: Int,
        markers: List<MapMarker>,
    ): Result<ByteArray> = mapDataSource.getStaticMap(
        width = width,
        height = height,
        markers = markers,
        dataVersion = getDataVersion(),
    )

    private suspend fun getDataVersion(): String {
        Log.d("test", mapDataVersionDataSource.isTimeToLiveValid.first().toString())

        if (mapDataVersionDataSource.isTimeToLiveValid.first()) {
            return mapDataVersionDataSource.dataVersion.first()
        }
        updateMapDataVersion()
        return mapDataVersionDataSource.dataVersion.first()
    }

    private suspend fun updateMapDataVersion() = mapDataSource.updateMapDataVersion()
        .onSuccess {
            mapDataVersionDataSource.setDataVersion(it.version)
            mapDataVersionDataSource.setTimeToLive(it.interval)
        }.onFailure { }
}