package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class RegisterCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        centerName: String,
        detailedAddress: String,
        introduce: String,
        lotNumberAddress: String,
        officeNumber: String,
        roadNameAddress: String
    ) = profileRepository.registerCenterProfile(
        centerName = centerName,
        detailedAddress = detailedAddress,
        introduce = introduce,
        lotNumberAddress = lotNumberAddress,
        officeNumber = officeNumber,
        roadNameAddress = roadNameAddress
    )
}