package com.idle.data.repository.map

import com.idle.domain.repositorry.map.MapRepository
import com.idle.network.source.map.MapDataSource
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapDataSource: MapDataSource,
) : MapRepository {
}