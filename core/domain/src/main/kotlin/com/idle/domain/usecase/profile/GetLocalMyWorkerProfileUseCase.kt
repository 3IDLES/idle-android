package com.idle.domain.usecase.profile

import com.idle.domain.model.profile.WorkerProfile
import com.idle.domain.repositorry.ProfileRepository
import javax.inject.Inject

class GetLocalMyWorkerProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<WorkerProfile> {
        return profileRepository.getLocalMyWorkerProfile().recoverCatching {
            profileRepository.getMyWorkerProfile().getOrThrow()
        }
    }
}
