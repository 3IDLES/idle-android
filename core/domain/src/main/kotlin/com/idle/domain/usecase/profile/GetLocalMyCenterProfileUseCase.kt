package com.idle.domain.usecase.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class GetLocalMyCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<CenterProfile> {
        return profileRepository.getLocalMyCenterProfile().recoverCatching {
            profileRepository.getMyCenterProfile().getOrThrow()
        }
    }
}