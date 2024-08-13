package com.idle.domain.usecase.map

import com.idle.domain.model.map.MapMarker
import com.idle.domain.repositorry.map.MapRepository
import javax.inject.Inject

class GetStaticMapUseCase @Inject constructor(
    private val mapRepository: MapRepository,
) {
    suspend operator fun invoke(
        width: Int,
        height: Int,
        markers: List<MapMarker>
    ): Result<ByteArray> = mapRepository.getStaticMap(
        width = width,
        height = height,
        markers = markers,
    )
}