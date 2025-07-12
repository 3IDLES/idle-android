package com.idle.domain.usecase.profile

import com.idle.domain.model.profile.CenterProfile
import com.idle.domain.repositorry.ProfileRepository
import javax.inject.Inject

class GetMyCenterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): CenterProfile = try {
        profileRepository.getLocalMyCenterProfile()
    } catch (e: Exception) {
        profileRepository.getMyCenterProfile()
    }
}
