package com.idle.data.repository.map

import android.util.Log
import com.idle.datastore.datasource.MapDataVersionDataSource
import com.idle.domain.repositorry.map.MapRepository
import com.idle.network.source.map.MapDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapDataSource: MapDataSource,
    private val mapDataVersionDataSource: MapDataVersionDataSource,
) : MapRepository {
    override suspend fun getStaticMap(
        center: String,
        level: Int,
        width: Int,
        height: Int,
    ): Result<ByteArray> = mapDataSource.getStaticMap(
        center = center,
        level = level,
        width = width,
        height = height,
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