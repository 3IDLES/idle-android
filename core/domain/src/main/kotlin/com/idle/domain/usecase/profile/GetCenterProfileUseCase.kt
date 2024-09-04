package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class GetCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(centerId: String) = profileRepository.getCenterProfile(centerId)
}