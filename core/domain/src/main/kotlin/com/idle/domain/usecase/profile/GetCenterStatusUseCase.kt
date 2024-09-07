package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class GetCenterStatusUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.getCenterStatus()
}