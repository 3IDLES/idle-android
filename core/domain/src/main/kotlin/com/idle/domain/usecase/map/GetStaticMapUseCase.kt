package com.idle.domain.usecase.map

import com.idle.domain.repositorry.map.MapRepository
import javax.inject.Inject

class GetStaticMapUseCase @Inject constructor(
    private val mapRepository: MapRepository,
) {
    suspend operator fun invoke(
        center: String,
        level: Int,
        width: Int,
        height: Int,
    ): Result<ByteArray> = mapRepository.getStaticMap(
        center = center,
        level = level,
        width = width,
        height = height,
    )
}