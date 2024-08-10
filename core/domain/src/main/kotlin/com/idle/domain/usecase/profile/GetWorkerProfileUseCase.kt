package com.idle.domain.usecase.profile

import com.idle.domain.repositorry.profile.ProfileRepository
import javax.inject.Inject

class GetWorkerProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(workerId: String) = profileRepository.getWorkerProfile(workerId)
}