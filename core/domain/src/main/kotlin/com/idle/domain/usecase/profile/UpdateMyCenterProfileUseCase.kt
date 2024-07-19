package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.CenterProfileRepository
import javax.inject.Inject

class UpdateMyCenterProfileUseCase @Inject constructor(
    private val centerProfileRepository: CenterProfileRepository
) {
    suspend operator fun invoke(
        centerName: String,
        officeNumber: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: Double,
        latitude: Double,
        introduce: String?,
        profileImageUrl: String?
    ) = centerProfileRepository.updateMyCenterProfile(
        centerName = centerName,
        officeNumber = officeNumber,
        lotNumberAddress = lotNumberAddress,
        detailedAddress = detailedAddress,
        longitude = longitude,
        latitude = latitude,
        introduce = introduce,
        profileImageUrl = profileImageUrl,
    )
}